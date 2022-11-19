
import java.lang.reflect.*;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws Exception {
//        Method method = MyClass.class.getMethod("someMethodCanReturnable");
//        System.out.println(isSubtype(method.getGenericReturnType(), ReturnableClass.class));
        Method method = M.class.getMethod("m4");
        System.out.println(isSubtype(method.getGenericReturnType(), SomeClass.class, null));
    }

    static Type[] getGenerics(Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            return parameterizedType.getActualTypeArguments();
        }
        return null;
    }


    public static boolean isSubtype(Type returnType, Type type, Type[] typeGenerics) {
        System.out.println();
        System.out.println("Type:       " + type);
        System.out.println("Generics:   " + Arrays.toString(typeGenerics));
        System.out.println("ReturnType: " + returnType);
        if (type == returnType || returnType == Object.class) return true;
        try {
            String typeName = type.toString().replaceAll("<.*>", "").replace("class ", "");
            String returnName = returnType.toString().replaceAll("<.*>", "").replace("class ", "");
            var typeClass = Class.forName(typeName);
            var returnClass = Class.forName(returnName);
            if (typeClass != returnClass) {
                System.out.println("Class:      " + typeClass);
                var interfacesGenerics = typeClass.getGenericInterfaces();
                var interfaces = typeClass.getInterfaces();
                var superClassGeneric = typeClass.getGenericSuperclass();
                var superClass = typeClass.getSuperclass();
                System.out.println("Super:      " + superClassGeneric);
                if (superClass != null && superClass != Object.class && returnClass.isAssignableFrom(superClass)) {
                    return isSubtype(returnType, superClassGeneric, typeGenerics == null ? getGenerics(type) : typeGenerics);
                }
                for (int i = 0; i < interfacesGenerics.length; i++) {
                    if (returnClass.isAssignableFrom(interfaces[i]))
                        return isSubtype(returnType, interfacesGenerics[i], typeGenerics == null ? getGenerics(type) : typeGenerics);
                }
            }
        } catch (Exception ignored) {
            System.out.println("Cant cast :" + ignored.getMessage());
        }

        System.out.println("Result:     " + type.getTypeName());

        if (type instanceof ParameterizedType myParameterizedType && returnType instanceof ParameterizedType parameterizedType) {
            var myActualArguments = typeGenerics == null ? myParameterizedType.getActualTypeArguments() : typeGenerics;
            var actualArguments = parameterizedType.getActualTypeArguments();
            if (myActualArguments.length != actualArguments.length) return false;
            for (int i = 0; i < actualArguments.length; i++) {
                Type myGenericType = myActualArguments[i];
                Type typeArgument = actualArguments[i];
                if (typeArgument instanceof WildcardType wildcardType) {
                    Type[] upper = wildcardType.getUpperBounds();
                    Type[] lower = wildcardType.getLowerBounds();
                    for (Type low : lower) {
                        if (!isSubtype(myGenericType, low, getGenerics(low))) return false;
                    }
                    for (Type up : upper) {
                        if (!isSubtype(up, myGenericType, getGenerics(myGenericType))) return false;
                    }
                } else {
                    if (!isSubtype(typeArgument, myGenericType, getGenerics(myGenericType))) return false;
                }
            }
            System.out.println("true");
            return true;
        }
        try {
            var returnClass = (Class<?>) returnType;
            var clazz = (Class<?>) type;
            return returnClass.isAssignableFrom(clazz);
        } catch (Exception ignored) {
        }
        System.out.println("false");
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
