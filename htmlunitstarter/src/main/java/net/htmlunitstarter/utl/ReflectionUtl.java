package net.htmlunitstarter.utl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.Assert;

/**
 * Provides utility functions on reflection of classes and objects
 */
public class ReflectionUtl {
   /** Callback interface invoked on each field in the hierarchy. */
   public interface FieldCallback {

      /**
       * Perform an operation using the given field.
       * @param field the field to operate on
       * @throws IllegalArgumentException 
       * @throws IllegalAccessException 
       */
      void doWith( Field field ) throws IllegalArgumentException, IllegalAccessException;
   }

   /**
    * Callback optionally used to filter fields to be operated on by a field callback.
    */
   public interface FieldFilter {

      /**
       * Determine whether the given field matches.
       * @param field the field to check
       * @return Flag that indicates the field matches
       */
      boolean matches( Field field );
   }

   /**
    * Action to take on each method.
    */
   public interface MethodCallback {

      /**
       * Perform an operation using the given method.
       * @param method the method to operate on
       * @throws IllegalArgumentException
       * @throws IllegalAccessException 
       */
      void doWith( Method method ) throws IllegalArgumentException, IllegalAccessException;
   }

   /**
    * Callback optionally used to filter methods to be operated on by a method callback.
    */
   public interface MethodFilter {

      /**
       * Determine whether the given method matches.
       * @param method the method to check
       * @return Flag that indicates the method applies
       */
      boolean matches( Method method );
   }

   /**
    * @param obj Instance of the class that to determine if field exists
    * @param fieldName Name of the field to get from the specified instance
    * @return Flag that indicates the field exists on the object
    */
   public static boolean containsField( Object obj, String fieldName ) {
      boolean result = false;
      try {
         result = findField( obj.getClass(), fieldName ) != null;
      }
      catch( Exception e ) {}
      return result;
   }

   /**
    * @param obj Object to determine if method exists
    * @param methodName Name of the method to invoke on the object
    * @param paramTypes Parameter types of the method
    * @return Flag that indicates the function exists
    */
   public static boolean containsFunction( Object obj, String methodName, Class<?>... paramTypes ) {
      return containsFunction( obj, methodName, Arrays.asList( paramTypes ) );
   }

   /**
    * @param obj Object to determine if method existss
    * @param methodName Name of the method to invoke on the object
    * @param paramTypes Parameter types of the method
    * @return Flag that indicates the function exists
    */
   public static boolean containsFunction( Object obj, String methodName, List<Class<?>> paramTypes ) {
      boolean result = false;
      try {
         final Class<?> clazz = obj.getClass();
         result = findMethod( clazz, methodName, paramTypes ) != null;
      }
      catch( Exception e ) {}
      return result;
   }

   /**
    * Invoke the given callback on all fields in the target class, going up the
    * class hierarchy to get all declared fields.
    * @param clazz the target class to analyze
    * @param fc the callback to invoke for each field
    * @throws IllegalArgumentException 
    */
   public static void doWithFields( Class<?> clazz, FieldCallback fc ) throws IllegalArgumentException {
      doWithFields( clazz, fc, null );
   }

   /**
    * Invoke the given callback on all fields in the target class, going up the
    * class hierarchy to get all declared fields.
    * @param clazz the target class to analyze
    * @param fc the callback to invoke for each field
    * @param ff the filter that determines the fields to apply the callback to
    * @throws IllegalArgumentException 
    */
   public static void doWithFields( Class<?> clazz, FieldCallback fc, FieldFilter ff ) throws IllegalArgumentException {

      // Keep backing up the inheritance hierarchy.
      Class<?> targetClass = clazz;
      do {
         Field[] fields = targetClass.getDeclaredFields();
         for( Field field : fields ) {
            // Skip static and final fields.
            if( ff != null && !ff.matches( field ) ) {
               continue;
            }
            try {
               fc.doWith( field );
            }
            catch( IllegalAccessException ex ) {
               throw new IllegalStateException(
                     "Shouldn't be illegal to access field '" + field.getName() + "': " + ex );
            }
         }
         targetClass = targetClass.getSuperclass();
      }
      while( targetClass != null && targetClass != Object.class );
   }

   /**
    * Perform the given callback operation on all matching methods of the given
    * class and super classes.
    * <p>The same named method occurring on subclass and superclass will appear
    * twice, unless excluded by a {@link MethodFilter}.
    * @param clazz class to start looking at
    * @param mc the callback to invoke for each method
    * @throws IllegalArgumentException 
    * @see #doWithMethods(Class, MethodCallback, MethodFilter)
    */
   public static void doWithMethods( Class<?> clazz, MethodCallback mc ) throws IllegalArgumentException {
      doWithMethods( clazz, mc, null );
   }

   /**
    * Perform the given callback operation on all matching methods of the given
    * class and super classes (or given interface and super-interfaces).
    * <p>The same named method occurring on subclass and superclass will appear
    * twice, unless excluded by the specified {@link MethodFilter}.
    * @param clazz class to start looking at
    * @param mc the callback to invoke for each method
    * @param mf the filter that determines the methods to apply the callback to
    * @throws IllegalArgumentException 
    */
   public static void doWithMethods( Class<?> clazz, MethodCallback mc, MethodFilter mf ) throws IllegalArgumentException {
      doWithMethods( clazz, mc, mf, true );
   }

   public static void doWithMethods( Class<?> clazz, MethodCallback mc, MethodFilter mf, boolean searchSuperClass ) throws IllegalArgumentException {

      // Keep backing up the inheritance hierarchy.
      Method[] methods = clazz.getDeclaredMethods();
      for( Method method : methods ) {
         if( mf != null && !mf.matches( method ) ) {
            continue;
         }
         try {
            mc.doWith( method );
         }
         catch( IllegalAccessException ex ) {
            throw new IllegalStateException( "Shouldn't be illegal to access method '" + method.getName()
                  + "': " + ex );
         }
      }
      if( searchSuperClass ) {
         if( clazz.getSuperclass() != null ) {
            doWithMethods( clazz.getSuperclass(), mc, mf );
         }
         else if( clazz.isInterface() ) {
            for( Class<?> superIfc : clazz.getInterfaces() ) {
               doWithMethods( superIfc, mc, mf );
            }
         }
      }
   }

   /**
    * Finds bean property for a specified variable name
    * @param clazz Class that should be searched for the specified variable
    * @param variableName Name of the field being search for on the class
    * @return Method that will Get the value of the variable
    */
   @SuppressWarnings("rawtypes")
   public static Method findBeanPropertyGetMethodForVariable( Class<? extends Object> clazz, String variableName ) {
      final String searchName = "get" + variableName;
      Method result = null;
      Class searchType = clazz;
      while( searchType != null && result == null ) {
         final Method[] methods = ( searchType.isInterface() ) ? searchType.getMethods() : searchType.getDeclaredMethods();
         for( Method method : methods ) {
            if( searchName.equalsIgnoreCase( method.getName() ) && method.getParameterTypes().length == 0 ) {
               result = method;
               break;
            }
         }
         searchType = searchType.getSuperclass();
      }
      return result;
   }

   /**
    * Finds bean property for the specified variable name
    * @param clazz Class that should be searched for the specified variable
    * @param variableName Name of the field being search for on the class
    * @return Method that will Set the value of the variable
    */
   public static Method findBeanPropertySetMethodForVariable( Class<? extends Object> clazz, String variableName ) {
      Method result = null;
      final List<Method> matches = findBeanPropertySetMethodsForVariable( clazz, variableName );
      if( matches.size() > 0 ) {
         result = matches.get( 0 );
      }
      return result;
   }

   /**
    * Finds bean property for the specified variable name
    * @param clazz Class that should be searched for the specified variable
    * @param variableName Name of the field being search for on the class
    * @param parameterClass Type of parameter to set
    * @return Method that will Set the value of the variable
    */
   public static Method findBeanPropertySetMethodForVariable( Class<? extends Object> clazz, String variableName, Class<?> parameterClass ) {
      Method result = null;
      final List<Method> matches = findBeanPropertySetMethodsForVariable( clazz, variableName );
      for( Method match : matches ) {
         if( match.getParameterTypes()[0].equals( parameterClass ) ) {
            result = match;
            break;
         }
      }
      return result;
   }

   /**
    * Finds bean property for the specified variable name that matches the parameter type best
    * @param clazz Class that should be searched for the specified variable
    * @param variableName Name of the field being search for on the class
    * @param parameterClass Type of parameter to set
    * @return Method that will Set the value of the variable
    */
   public static Method findBeanPropertySetMethodForVariableBestMatch( Class<? extends Object> clazz, String variableName, Class<?> parameterClass ) {
      Method result = findBeanPropertySetMethodForVariable( clazz, variableName, parameterClass );
      if( result == null ) {
         result = findBeanPropertySetMethodForVariable( clazz, variableName );
      }
      return result;
   }

   /**
    * Finds the best match for a given class in a list of classes
    * @param classes List of classes that might be assignable to the specified class
    * @param clazz Class to find a closest fit for
    * @return Class that is the closest fit; if no assignable are found <code>null</code> is returned
    */
   @SuppressWarnings({ "rawtypes", "unchecked" })
   public static Class findClosestMatch( List<Class> classes, Class clazz ) {
      Class result = null;
      for( Class candidate : classes ) {
         if( candidate.isAssignableFrom( clazz ) ) {
            if( result == null || result.isAssignableFrom( candidate ) ) {
               result = candidate;
            }
         }
      }
      return result;
   }

   /**
    * Tries to get the specified annotation from the method and will go up the inheritance
    * hierarchy to find the specified annotation if it exists.
    * @param method Method to get the annotation from
    * @param annotationClass Annotation class that is being search for on the method
    * @return Annotation instance closest to the specified method
    */
   public static <T extends Annotation> T getAnnotation( Method method, Class<T> annotationClass ) {
      T result = null;
      if( method != null ) {
         result = method.getAnnotation( annotationClass );
         if( result == null ) {
            for( Class<?> interfaceClass : method.getDeclaringClass().getInterfaces() ) {
               final Method interfaceMethod = org.springframework.util.ReflectionUtils.findMethod( interfaceClass, method.getName(), method.getParameterTypes() );
               result = getAnnotation( interfaceMethod, annotationClass );
               if( result != null ) {
                  break;
               }
            }
            if( result == null ) {
               final Class<?> parent = method.getDeclaringClass().getSuperclass();
               if( parent != null ) {
                  final Method parentMethod = org.springframework.util.ReflectionUtils.findMethod( parent, method.getName(), method.getParameterTypes() );
                  result = getAnnotation( parentMethod, annotationClass );
               }
            }
         }
      }
      return result;
   }

   /**
    * @param <FieldType> Type of field that should be expected from the specified field
    * @param obj Instance of the class that have the value extracted from it
    * @param fieldName Name of the field to get from the specified instance
    * @return Value of the specified field of the specified instance
    */
   @SuppressWarnings("unchecked")
   public static <FieldType> FieldType getField( Object obj, String fieldName ) {
      try {
         return ( FieldType )findField( obj.getClass(), fieldName ).get( obj );
      }
      catch( Exception exception ) {
         throw new RuntimeException( exception );
      }
   }

   /**
    * Gets a list of all fields that pass the specified filter
    * @param clazz the target class to analyze
    * @param ff the filter that determines the fields to get
    * @return List of fields that pass the specified filter
    */
   public static List<Field> getFields( Class<?> clazz, FieldFilter ff ) {
      final List<Field> result = new ArrayList<Field>();
      doWithFields( clazz, new FieldCallback() {
         @Override
         public void doWith( Field field ) throws IllegalArgumentException, IllegalAccessException {
            result.add( field );
         }
      }, ff );
      return result;
   }

   /**
    * Gets a list of all methods that pass the specified filter
    * @param clazz the target class to analyze
    * @param mf the filter that determines the methods to get
    * @return List of methods that pass the specified filter
    */
   public static List<Method> getMethods( Class<?> clazz, MethodFilter mf ) {
      return getMethods( clazz, mf, true );
   }

   public static List<Method> getMethods( Class<?> clazz, MethodFilter mf, boolean searchSuperClass ) {
      final List<Method> result = new ArrayList<Method>();
      doWithMethods( clazz, new MethodCallback() {
         @Override
         public void doWith( Method method ) throws IllegalArgumentException, IllegalAccessException {
            result.add( method );
         }
      }, mf, searchSuperClass );
      return result;
   }

   /**
    * Returns a list of methods from the provided clazz with the specified annotation class annotated 
    * @param clazz
    * @param annotationClass
    * @return List of Methods
    */
   public static List<Method> getMethodsWithAnnotation( Class<?> clazz, Class<? extends Annotation> annotationClass ) {
      final List<Method> result = new ArrayList<Method>();
      for( Method method : clazz.getMethods() ) {
         if( getAnnotation( method, annotationClass ) != null ) {
            result.add( method );
         }
      }
      return result;
   }

   @SuppressWarnings("rawtypes")
   public static Class getParameterizedType( Class<?> clazz ) {
      return getParameterizedType( clazz, 0 );
   }

   @SuppressWarnings("rawtypes")
   public static Class getParameterizedType( Class<?> clazz, int parameterIndex ) {
      return ( Class )( ( ParameterizedType )clazz.getGenericSuperclass() ).getActualTypeArguments()[parameterIndex];
   }

   /**
    * Finds the {@link ParameterizedType} for an implementing class of an interface that has generics
    * @param implementationClass Implementing class of the generic interface
    * @param interfaceClass Interface that has generics
    * @return {@link ParameterizedType} for the implementing class if it exists otherwise <code>null</code>
    */
   public static ParameterizedType getParameterizedInterface( Class<?> implementationClass, Class<?> interfaceClass ) {
      ParameterizedType result = null;
      for( Type type : implementationClass.getGenericInterfaces() ) {
         if( type instanceof ParameterizedType ) {
            final ParameterizedType paramType = ( ParameterizedType )type;
            if( ( ( Class<?> )paramType.getRawType() ).isAssignableFrom( interfaceClass ) ) {
               result = paramType;
               break;
            }
         }
      }
      return result;
   }

   public static Class<?> getParameterizedType( Field field ) {
      return getParameterizedType( field, 0 );
   }

   public static Class<?> getParameterizedType( Field field, int idx ) {
      return getParameterizedType( ( ParameterizedType )field.getGenericType(), idx );
   }

   public static Class<?> getParameterizedType( Method method ) {
      return getParameterizedType( method, 0 );
   }

   public static Class<?> getParameterizedType( Method method, int idx ) {
      return getParameterizedType( ( ParameterizedType )method.getGenericReturnType(), idx );
   }

   private static Class<?> getParameterizedType( ParameterizedType type, int idx ) {
      Class<?> result = null;
      if( type != null ) {
         if( type.getActualTypeArguments().length > idx ) {
            final Object typeArg = type.getActualTypeArguments()[idx];
            if( typeArg instanceof Class ) {
               result = ( Class<?> )type.getActualTypeArguments()[idx];
            }
            else if( typeArg instanceof ParameterizedType ) {
               final ParameterizedType paramType = ( ParameterizedType )type.getActualTypeArguments()[idx];
               result = ( Class<?> )paramType.getRawType();
            }
            else {
               throw new RuntimeException( String.format( "Unknown Type Argument: %s", typeArg.getClass() ) );
            }
         }
      }
      return result;
   }

   /**
    * Invokes a method on a specified object
    * @param obj Object to invoke the specified method on
    * @param methodName Name of the method to invoke on the object
    * @param parameterTypes Parameter types of the method
    * @param parameters Parameters to pass to the method
    * @return Result of the call to the method
    */
   public static Object invoke( final Object obj, final String methodName, final List<Class<?>> parameterTypes, final List<Object> parameters ) {
      if( obj == null ) {
         return null;
      }
      try {
         final Class<?> clazz = obj.getClass();
         final Method method = findMethod( clazz, methodName, parameterTypes );
         return invoke( obj, method, parameterTypes, parameters );
      }
      catch( Exception exception ) {
         throw new RuntimeException( exception );
      }
   }

   /**
    * Invokes a method on a specified object
    * @param obj Object to invoke the specified method on
    * @param method method to invoke on the object
    * @param parameterTypes Parameter types of the method
    * @param parameters Parameters to pass to the method
    * @return Result of the call to the method
    */
   public static Object invoke( final Object obj, final Method method, final List<Class<?>> parameterTypes, final List<Object> parameters ) {
      if( obj == null || method == null ) {
         return null;
      }
      try {
         method.setAccessible( true );
         return method.invoke( obj, parameters.toArray() );
      }
      catch( Exception exception ) {
         throw new RuntimeException( exception );
      }
   }

   /**
    * Invokes a static method of a specified class
    * @param clazz Class to invoke the static method of
    * @param methodName Name of the static method to invoke
    * @param parameterTypes Parameter types of the static method
    * @param parameters Parameters to pass to the static method
    * @return Result of the call to the static method
    */
   public static Object invokeStatic( Class<?> clazz, String methodName, List<Class<?>> parameterTypes, List<Object> parameters ) {
      try {
         final Method method = findMethod( clazz, methodName, parameterTypes );
         method.setAccessible( true );
         return method.invoke( null, parameters.toArray() );
      }
      catch( Throwable exception ) {
         throw new RuntimeException( String.format( "Error Invoking static methdod: %s:%s", clazz, methodName ), exception );
      }
   }

   /**
    * Creates a new instance of the specified class with the specified parameters
    * @param clazz Class to create the new instance of
    * @param params Parameters to pass into the constructor of the class
    * @return New instance of the class if a constructor could be found for the parameters
    */
   @SuppressWarnings({ "rawtypes", "unchecked" })
   public static <T> T newInstance( Class<T> clazz, Object... params ) {
      final Constructor constructor = findConstructor( clazz, params );
      Assert.notNull( constructor, "No constructor could be found on class: " + clazz + " that matchs paramaters: " + params );
      try {
         constructor.setAccessible( true );
         return ( T )constructor.newInstance( params );
      }
      catch( Exception exception ) {
         throw new RuntimeException( "Error creating new instance of " + clazz + " with constructor: " + constructor, exception );
      }
   }

   /**
    * @param obj Instance of the class that have the value set
    * @param fieldName Name of the field to set in the specified instance
    * @param value Value to set to the specified field of the instance
    */
   public static void setField( Object obj, String fieldName, Object value ) {
      try {
         findField( obj.getClass(), fieldName ).set( obj, value );
      }
      catch( Exception exception ) {
         throw new RuntimeException( exception );
      }
   }

   private static List<Method> findBeanPropertySetMethodsForVariable( Class<? extends Object> clazz, String variableName ) {
      final List<Method> result = new ArrayList<Method>();
      final String searchName = "set" + variableName;
      Class<?> searchType = clazz;
      while( searchType != null ) {
         final Method[] methods = ( searchType.isInterface() ) ? searchType.getMethods() : searchType.getDeclaredMethods();
         for( Method method : methods ) {
            if( searchName.equalsIgnoreCase( method.getName() ) && method.getParameterTypes().length == 1 ) {
               result.add( method );
            }
         }
         searchType = searchType.getSuperclass();
      }
      return result;
   }

   @SuppressWarnings("rawtypes")
   private static Constructor findConstructor( Class clazz, Object... params ) {
      Constructor result = null;
      final Constructor[] constructors = clazz.getDeclaredConstructors();
      for( Constructor constructor : constructors ) {
         if( params.length == constructor.getGenericParameterTypes().length ) {
            result = constructor;
            break;
         }
      }
      return result;
   }

   @SuppressWarnings("rawtypes")
   private static Field findField( Class clazz, String fieldName ) {
      Class currentClazz = clazz;
      Field result = null;
      while( result == null ) {
         try {
            result = currentClazz.getDeclaredField( fieldName );
            result.setAccessible( true );
         }
         catch( NoSuchFieldException exception ) {
            if( currentClazz == Object.class ) {
               throw new RuntimeException( exception );
            }
            currentClazz = currentClazz.getSuperclass();
         }
      }
      return result;
   }

   public static Method findMethod( final Class<?> clazz, final String methodName, final List<Class<?>> parameterTypes ) {
      Class<?> currentClazz = clazz;
      Method result = null;
      while( result == null ) {
         try {
            result = currentClazz.getDeclaredMethod( methodName, toArray( parameterTypes ) );
         }
         catch( NoSuchMethodException exception ) {
            if( currentClazz == Object.class ) {
               throw new RuntimeException( exception );
            }
            currentClazz = currentClazz.getSuperclass();
         }
      }
      return result;
   }

   /**
    * @param <FieldType> Type of field that should be expected from the specified field
    * @param clazz Class that contains the static field to get the value of
    * @param fieldName Name of the static field in the class
    * @return Value of the static field in the specified class
    */
   @SuppressWarnings("unchecked")
   public static <FieldType> FieldType getStaticField( Class<?> clazz, String fieldName ) {
      try {
         return ( FieldType )findField( clazz, fieldName ).get( null );
      }
      catch( Exception exception ) {
         throw new RuntimeException( exception );
      }
   }

   private static Class<?>[] toArray( List<Class<?>> classes ) {
      final Class<?>[] result = new Class[classes.size()];
      for( int index = 0; index < classes.size(); index++ ) {
         result[index] = classes.get( index );
      }
      return result;
   }
}
