package fmi.spring.framework.cookingRecipes.exception;

public class NoPermissionForPostEdit extends RuntimeException {
    public NoPermissionForPostEdit() {
    }

    public NoPermissionForPostEdit(String message) {
        super(message);
    }

    public NoPermissionForPostEdit(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPermissionForPostEdit(Throwable cause) {
        super(cause);
    }
}
