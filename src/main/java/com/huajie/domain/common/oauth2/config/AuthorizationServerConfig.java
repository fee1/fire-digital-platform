package com.huajie.domain.common.oauth2.config;

import com.huajie.domain.common.constants.CommonConstants;
import com.huajie.domain.common.oauth2.UserOauth2ServiceImpl;
import com.huajie.domain.common.oauth2.WechatTokenGranter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证服务器配置
 *
 * @author zhuxiaofeng
 * @date 2023/7/11
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserOauth2ServiceImpl userOauth2ServiceImpl;

    @Autowired
    private AuthenticationManager authenticationManager;


    /**
     * 使用密码模式需要配置 (项目目前使用的模式)
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenGranter(new TokenGranter() {
                    private CompositeTokenGranter delegate;
                    @Override
                    public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
                        if (delegate == null) {
                            delegate = new CompositeTokenGranter(getDefaultTokenGranters());
                        }
                        return delegate.grant(grantType, tokenRequest);
                    }

                    private List<TokenGranter> getDefaultTokenGranters() {
                        ClientDetailsService clientDetails = endpoints.getClientDetailsService();
                        AuthorizationServerTokenServices tokenServices = endpoints.getTokenServices();
                        AuthorizationCodeServices authorizationCodeServices = endpoints.getAuthorizationCodeServices();
                        OAuth2RequestFactory requestFactory = endpoints.getOAuth2RequestFactory();

                        List<TokenGranter> tokenGranters = new ArrayList<TokenGranter>();
                        tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetails,
                                requestFactory));
                        tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetails, requestFactory));
                        ImplicitTokenGranter implicit = new ImplicitTokenGranter(tokenServices, clientDetails, requestFactory);
                        tokenGranters.add(implicit);
                        tokenGranters.add(new ClientCredentialsTokenGranter(tokenServices, clientDetails, requestFactory));
                        if (authenticationManager != null) {
                            tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices,
                                    clientDetails, requestFactory));
                            tokenGranters.add(new WechatTokenGranter(authenticationManager, tokenServices, clientDetails, requestFactory));
                        }
                        return tokenGranters;
                    }

                })
                .authenticationManager(authenticationManager)
                .userDetailsService(userOauth2ServiceImpl);
    }

    /**
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(CommonConstants.CLIENT_ID)//配置client_id
                .secret(passwordEncoder.encode(CommonConstants.SECRET))//配置client_secret
                .accessTokenValiditySeconds(14400)//配置访问token的有效期
                .refreshTokenValiditySeconds(864000)//配置刷新token的有效期
                .redirectUris("http://127.0.0.1:8080/user/login")//配置redirect_uri，用于授权成功后跳转
                .scopes(CommonConstants.ALL)//配置申请的权限范围
                .authorizedGrantTypes("authorization_code",CommonConstants.PASSWORD, CommonConstants.WECHAT);//配置grant_type，表示授权类型
    }

}
