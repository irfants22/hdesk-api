package personal.api.user;

import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> search(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return cb.conjunction();
            }
            String pattern = "%" + keyword.trim().toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("email")), pattern));
        };
    }

    public static Specification<User> hasRole(String roleName) {
        return (root, query, cb) -> {
            if (roleName == null || roleName.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(cb.upper(
                    root.join("role").get("name")),
                    roleName.trim().toUpperCase());
        };
    }

    public static Specification<User> hasActive(Boolean status) {
        return (root, query, cb) -> status == null
                ? cb.conjunction()
                : cb.equal(root.get("active"), status);
    }
}
