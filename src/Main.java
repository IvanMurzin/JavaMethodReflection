
import java.lang.reflect.*;
import java.util.Arrays;

public class Main {
    static void log() {
        System.out.println();
    }

    static void log(Object message) {
        System.out.println("\u001B[34m" + message + "\u001B[0m");
    }

    static Type[] getGenerics(Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            return parameterizedType.getActualTypeArguments();
        }
        return null;
    }

    static Class<?> getClass(Type type) {
        try {
            String name = type.toString().replaceAll("<.*>", "").replace("class ", "");
            return Class.forName(name);
        } catch (Exception e) {
            return null;
        }
    }


    public static void main(String[] args) throws Exception {
        Method method = M.class.getMethod("m4");
        System.out.println(isSubtype(method.getGenericReturnType(), SomeClass.class, null));
    }

    public static boolean isSubtype(Type returnType, Type type, Type[] typeGenerics) {
        log();
        log("Type:       " + type);
        log("Generics:   " + Arrays.toString(typeGenerics));
        log("ReturnType: " + returnType);
        if (type == returnType || returnType == Object.class) return true;

        var typeClass = getClass(type);
        var returnClass = getClass(returnType);

        if (typeClass != null && returnClass != null && typeClass != returnClass) {
            log("Class:      " + typeClass);
            var interfacesGenerics = typeClass.getGenericInterfaces();
            var interfaces = typeClass.getInterfaces();
            var superClassGeneric = typeClass.getGenericSuperclass();
            var superClass = typeClass.getSuperclass();
            log("Super:      " + superClassGeneric);
            if (superClass != null && superClass != Object.class && returnClass.isAssignableFrom(superClass)) {
                return isSubtype(returnType, superClassGeneric, typeGenerics == null ? getGenerics(type) : typeGenerics);
            }
            for (int i = 0; i < interfacesGenerics.length; i++) {
                if (returnClass.isAssignableFrom(interfaces[i]))
                    return isSubtype(returnType, interfacesGenerics[i], typeGenerics == null ? getGenerics(type) : typeGenerics);
            }
        }

        log("Result:     " + type.getTypeName());

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
            log("true");
            return true;
        }
        try {
            var returnClassCasted = (Class<?>) returnType;
            var classCasted = (Class<?>) type;
            return returnClassCasted.isAssignableFrom(classCasted);
        } catch (Exception ignored) {
        }
        log("false");
        return false;
    }
}
