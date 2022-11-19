
import java.lang.reflect.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Method method = MyClass.class.getMethod("someMethodCanReturnable");
        System.out.println(isSubtype(method.getGenericReturnType(), ReturnableClass.class));
    }


    public static boolean isSubtype(Type returnType, Type type) {
        System.out.println("My type: " + type);
        System.out.println("ReturnType with generics: " + returnType);
        if (type == returnType || returnType == Object.class) return true;
        try {
            var returnClass = (Class<?>) type;
            var interfacesGenerics = ((Class<?>) type).getGenericInterfaces();
            var interfaces = ((Class<?>) type).getInterfaces();
            var superClassGeneric = ((Class<?>) type).getGenericSuperclass();
            var superClass = ((Class<?>) type).getSuperclass();
            if (superClass != null && superClass != Object.class && superClass.isAssignableFrom(returnClass)) {
                return isSubtype(returnType, superClassGeneric);
            }
            for (int i = 0; i < interfacesGenerics.length; i++) {
                if (interfaces[i].isAssignableFrom(returnClass))
                    return isSubtype(returnType, interfacesGenerics[i]);
            }
        } catch (Exception ignored) {
        }

        System.out.println(type.getTypeName());

        if (type instanceof ParameterizedType myParameterizedType && returnType instanceof ParameterizedType parameterizedType) {
            var myActualArguments = myParameterizedType.getActualTypeArguments();
            var actualArguments = parameterizedType.getActualTypeArguments();
            if (myActualArguments.length != actualArguments.length) return false;
            for (int i = 0; i < actualArguments.length; i++) {
                Type myGenericType = myActualArguments[i];
                Type typeArgument = actualArguments[i];
                if (typeArgument instanceof WildcardType wildcardType) {
                    Type[] upper = wildcardType.getUpperBounds();
                    Type[] lower = wildcardType.getLowerBounds();
                    for (Type low : lower) {
                        if (!isSubtype(myGenericType, low)) return false;
                    }
                    for (Type up : upper) {
                        if (!isSubtype(up, myGenericType)) return false;
                    }
                } else {
                    if (!isSubtype(typeArgument, myGenericType)) return false;
                }
            }
            return true;
        }
        try {
            var returnClass = (Class<?>) returnType;
            var clazz = (Class<?>) type;
            return returnClass.isAssignableFrom(clazz);
        } catch (Exception ignored) {
        }
        return false;
    }
}


//
//Class<?> superClass = clazz.getSuperclass();
//        Class<?>[] superInterfaces = clazz.getInterfaces();
//        if (superClass != Object.class && superClass != null && superClass.isAssignableFrom(clazz)) {
//            return canReturn(method, superClass);
//        }
//        for (Class<?> superInterface : superInterfaces) {
//            if (superInterface.isAssignableFrom(clazz)) {
//                return canReturn(method, superInterface);
//            }
//        }
