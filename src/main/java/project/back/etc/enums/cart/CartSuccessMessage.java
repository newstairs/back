package project.back.etc.enums.cart;

public enum CartSuccessMessage {
    SUCCESS_SEARCH("검색에 성공 했습니다."),
    SUCCESS_ADD_PRODUCT("장바구니에 '%s'이(가) 담겼습니다."),
    SUCCESS_UPDATE_CART("'%s'의 수량을 변경했습니다.");

    private String message;

    CartSuccessMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
