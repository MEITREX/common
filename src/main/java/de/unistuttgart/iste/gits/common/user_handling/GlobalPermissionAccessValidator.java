package de.unistuttgart.iste.gits.common.user_handling;

import de.unistuttgart.iste.gits.common.exception.MissingGlobalPermissionsException;

import java.util.Set;

public class GlobalPermissionAccessValidator {

    /**
     * Checks if the user has at least one of the required roles to access this resource.
     *
     * @param user the user to validate
     * @param roles the roles that allow access to this resource
     */
    public static void validateUserHasGlobalPermission(final LoggedInUser user, final Set<LoggedInUser.RealmRole> roles) {
        if (validateIsSuperUser(user)) {
            return;
        }
        final boolean containsRole = user.getRealmRoles().stream().anyMatch(roles::contains);
        if (!containsRole) {
            throw new MissingGlobalPermissionsException();
        }
    }

    /**
     * Checks if the user is a super-user.
     * @param user the User to validate
     * @return true if the user is a super user, false otherwise
     */
    public static boolean validateIsSuperUser(final LoggedInUser user) {
        return user.getRealmRoles().contains(LoggedInUser.RealmRole.SUPER_USER);
    }
}
