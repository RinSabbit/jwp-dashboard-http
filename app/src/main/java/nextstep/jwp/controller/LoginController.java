package nextstep.jwp.controller;

import static nextstep.jwp.controller.DefaultController.INDEX_RESOURCE_PATH;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.http.common.Body;
import nextstep.jwp.http.request.HttpRequest;
import nextstep.jwp.http.request.requestline.RequestPath;
import nextstep.jwp.http.response.HttpResponse;
import nextstep.jwp.http.session.HttpCookie;
import nextstep.jwp.http.session.HttpSession;
import nextstep.jwp.http.session.HttpSessions;
import nextstep.jwp.http.util.ParamExtractor;
import nextstep.jwp.model.User;

public class LoginController extends AbstractController {

    private static final String LOGIN_RESOURCE_PATH = "/login.html";
    private static final String UNAUTHORIZED_PATH = "/401.html";

    @Override
    protected void doGet(HttpRequest request,
        HttpResponse response) {
        Object user = request.getSession().getAttribute("user");
        if (Objects.nonNull(user)) {
            response.redirect2(INDEX_RESOURCE_PATH);
        }
        response.of2(new RequestPath(LOGIN_RESOURCE_PATH));
    }

    @Override
    protected void doPost(HttpRequest request,
        HttpResponse response) {
        final Body body = request.getBody();
        final Map<String, String> params = ParamExtractor.extractParams(body.asString());
        String account = params.get("account");
        String password = params.get("password");

        login(request, response, account, password);
        handleSession(request, response);
    }

    private void login(HttpRequest request, HttpResponse response, String account,
        String password) {
        final Optional<User> user = InMemoryUserRepository.findByAccount(account);
        if (user.isPresent()) {
            User presentUser = user.get();

            if (presentUser.checkPassword(password)) {
                final HttpSession httpSession = request.getSession();
                httpSession.setAttribute("user", user);
                response.redirect2(INDEX_RESOURCE_PATH);
                return;
            }
            response.redirect2(UNAUTHORIZED_PATH);
        }
    }

    private void handleSession(HttpRequest request, HttpResponse response) {
        String sessionId = request.getSession().getId();
        if (!HttpSessions.isValid(sessionId)) {
            HttpCookie cookie = new HttpCookie(Map.of("JSESSIONID", sessionId));
            cookie.addCookie(sessionId);
            response.setCookie(cookie);
            HttpSessions.putSession(request.getSession());
        }
    }
}
