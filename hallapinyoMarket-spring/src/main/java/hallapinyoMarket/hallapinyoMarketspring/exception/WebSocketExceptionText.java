package hallapinyoMarket.hallapinyoMarketspring.exception;

import org.springframework.web.socket.TextMessage;

public class WebSocketExceptionText {
    public static TextMessage returnErrorMessage1() {
        return new TextMessage("{\n" +
                " \"type\" : \"ERROR\",\n" +
                " \"roomId\" : 0,\n" +
                " \"senderUserId\" : \"\",\n" +
                " \"message\" : \"세션이 유효하지 않습니다. 웹 소켓을 종료합니다.\"\n" +
                "}");
    }
    public static TextMessage returnErrorMessage2() {
        return new TextMessage("{\n" +
                " \"type\" : \"ERROR\",\n" +
                " \"roomId\" : 0,\n" +
                " \"senderUserId\" : \"\",\n" +
                " \"message\" : \"이상한 ID 값입니다. 웹소켓을 종료합니다.\"\n" +
                "}");
    }
    public static TextMessage returnErrorMessage3() {
        return new TextMessage("{\n" +
                " \"type\" : \"ERROR\",\n" +
                " \"roomId\" : 0,\n" +
                " \"senderUserId\" : \"\",\n" +
                " \"message\" : \"세션값과 senderUserId 값이 일치 하지 않습니다. 웹소켓을 종료합니다.\"\n" +
                "}");
    }
    public static TextMessage returnErrorMessage4() {
        return new TextMessage("{\n" +
                " \"type\" : \"ERROR\",\n" +
                " \"roomId\" : 0,\n" +
                " \"senderUserId\" : \"\",\n" +
                " \"message\" : \"ENTER를 중복하였습니다. 웹소켓을 종료합니다.\"\n" +
                "}");
    }
    public static TextMessage returnErrorMessage5() {
        return new TextMessage("{\n" +
                " \"type\" : \"ERROR\",\n" +
                " \"roomId\" : 0,\n" +
                " \"senderUserId\" : \"\",\n" +
                " \"message\" : \"TYPE 값이 이상합니다. 웹소켓을 종료합니다.\"\n" +
                "}");
    }
}
