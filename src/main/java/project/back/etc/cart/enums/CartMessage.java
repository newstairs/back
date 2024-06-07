package project.back.etc.cart.enums;

public enum CartMessage {
    // 실패
    NOT_EXIST_PRODUCT("'%s'은(는) 존재하지 않는 상품입니다."),
    NOT_FOUND_MEMBER("사용자 정보를 찾을 수 없습니다."),
    NOT_FOUND_PRODUCT("상품을 찾을 수 없습니다."),
    ALREADY_EXIST_PRODUCT("이미 장바구니에 존재하는 상품입니다."),
    INVALID_QUANTITY("1 이상의 숫자만 입력해주세요"),
    QUANTITY_ONE_OR_MORE("수량은 1개 이상이어야 합니다."),
    DELETE_RECOMMEND("삭제를 원한다면 삭제버튼을 눌러주세요"),
    NOT_EXIST_PRODUCT_IN_CART("장바구니에 존재하지 않는 상품입니다."),
    // 성공
    SUCCESS_GET("장바구니 조회에 성공했습니다."),
    SUCCESS_SEARCH("상품 검색에 성공 했습니다."),
    SUCCESS_ADD("장바구니에 '%s'이(가) 담겼습니다."),
    SUCCESS_UPDATE("'%s'의 수량을 변경했습니다."),
    SUCCESS_DELETE("장바구니에서 '%s'을(를) 삭제했습니다."),
    SUCCESS_DELETE_ALL("장바구니 비우기에 성공했습니다.");

    private String message;

    CartMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
