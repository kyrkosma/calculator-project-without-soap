package org.springframework.samples.mvc.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Necessary for WebSecurity (class: WebMvcSecurityConfig) to work.
 */
public class SecurityWebInitializer extends AbstractSecurityWebApplicationInitializer {
}