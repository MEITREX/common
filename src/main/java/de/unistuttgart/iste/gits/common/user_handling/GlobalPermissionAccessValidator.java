package de.unistuttgart.iste.gits.common.user_handling;

import de.unistuttgart.iste.gits.common.exception.MissingGlobalPermissionsException;

public class GlobalPermissionAccessValidator {

    public static void validateUserHasGlobalPermission(final LoggedInUser user, final LoggedInUser.RealmRole role) {
        if (validateIsSuperUser(user)) {
            return;
        }
        if (!user.getRealmRoles().contains(role)) {
            throw new MissingGlobalPermissionsException();
        }
    }

    public static boolean validateIsSuperUser(final LoggedInUser user) {
        return user.getRealmRoles().contains(LoggedInUser.RealmRole.SUPER_USER);
    }
}
