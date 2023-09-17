package in.das.jwtdemo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.das.jwtdemo.exception.UnAuthorisedResourceAccessException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String headerToken = request.getHeader("token");
        String usernameToken = request.getHeader("user");
        log.info("Header: {}",headerToken);
        log.info("usernameToken: {}",usernameToken);
        String username = null;
        String token = null;
        try{
            if(headerToken != null){
                token = headerToken;
                username = jwtHelper.getUsernameFromToken(token);
                log.info("username= {}",username);
                if(usernameToken != null && !usernameToken.equals(username)){
                    throw new UnAuthorisedResourceAccessException("unauthorized access, username & token mismatch");
                }
            } else {
                logger.info("Invalid Header Value !! ");
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //fetch user detail from username
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                log.info("userDetails = {}", new ObjectMapper().writeValueAsString(userDetails));
                Boolean validateToken = jwtHelper.validateToken(token, userDetails);
                if (validateToken) {
                    //set the authentication
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    logger.info("Validation fails !!");
                }
            }
            filterChain.doFilter(request, response);
        }catch (UnAuthorisedResourceAccessException ex){
            log.info("UnAuthorisedResourceAccessException occurred");
            response.setContentType("application/json");
            response.setStatus(403);
            response.getWriter().write("error response");
        }catch (ExpiredJwtException ex){
            ex.printStackTrace();
            response.setContentType("application/json");
            response.setStatus(403);
            response.getWriter().write("jwt token expired");
        }catch (Exception ex){
            ex.printStackTrace();
            response.setContentType("application/json");
            response.setStatus(403);
            response.getWriter().write("An Exception occurred");
        }
    }

}
