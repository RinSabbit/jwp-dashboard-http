package nextstep.jwp.controller;

import java.util.concurrent.locks.ReentrantLock;
import nextstep.jwp.http.request.HttpRequest;
import nextstep.jwp.http.request.requestline.RequestPath;
import nextstep.jwp.http.response.HttpResponse;
import nextstep.jwp.http.response.HttpStatus;

public class DefaultController extends AbstractController {

    public static final String INDEX_RESOURCE_PATH = "/index.html";

    @Override
    protected void doGet(HttpRequest request,
        HttpResponse response) {
        response.of2(new RequestPath(INDEX_RESOURCE_PATH));
    }
}
