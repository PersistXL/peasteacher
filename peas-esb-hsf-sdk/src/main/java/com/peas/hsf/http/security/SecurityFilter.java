package com.peas.hsf.http.security;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.peas.hsf.tool.Checkers;
import org.glassfish.jersey.internal.util.Base64;
import org.glassfish.jersey.server.ContainerRequest;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

/**
 * 鉴权过滤器
 *
 * @author dyh
 */
@Provider
@PreMatching
public class SecurityFilter implements ContainerRequestFilter {

    @Inject
    private javax.inject.Provider<UriInfo> uriInfo;

    private AuthenticationVerifier verifier;

    public SecurityFilter(AuthenticationVerifier verifier) {
        Preconditions.checkArgument(verifier != null, "AuthenticationVerifier can not be null");
        this.verifier = verifier;
    }

    @Override
    public void filter(ContainerRequestContext filterContext) throws IOException {
        User user = authenticate(filterContext.getRequest());
        filterContext.setSecurityContext(new Authorizer(user));
    }

    private User authenticate(Request request) {
        String authentication = ((ContainerRequest) request).getHeaderString(HttpHeaders.AUTHORIZATION);
        authentication = Checkers.checkNotNull(authentication,
                "Authentication credentials are required");
        Checkers.checkState(authentication.startsWith("Basic "));
        authentication = authentication.substring("Basic ".length());
        String[] values = Base64.decodeAsString(authentication).split(":");
        Checkers.checkState(values.length >= 2);
        String username = values[0];
        String password = values[1];
        Checkers.checkState(!Strings.isNullOrEmpty(username) && !Strings.isNullOrEmpty(password));
        Checkers.checkArgument(verifier.verify(username, password),
                "Invalid username or password");
        return new User("user", "user");
    }

    public class Authorizer implements SecurityContext {
        private User user;

        private Principal principal;

        public Authorizer(final User user) {
            this.user = user;
            this.principal = new Principal() {
                public String getName() {
                    return user.username;
                }
            };
        }

        public Principal getUserPrincipal() {
            return this.principal;
        }

        public boolean isUserInRole(String role) {
            return (role.equals(user.role));
        }

        public boolean isSecure() {
            return "https".equals(uriInfo.get().getRequestUri().getScheme());
        }

        public String getAuthenticationScheme() {
            return SecurityContext.BASIC_AUTH;
        }
    }

    public static class User {
        public String username;

        public String role;

        public User(String username, String role) {
            this.username = username;
            this.role = role;
        }
    }
}
