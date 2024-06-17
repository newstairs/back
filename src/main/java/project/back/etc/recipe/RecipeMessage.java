package project.back.etc.recipe;

public enum RecipeMessage {
    /**
     * SUCCESS
     */
    LOADED_RECIPE("레시피 성공적으로 불러왔습니다."),
    LOADED_RECIPE_LIST("레시피 목록을 성공적으로 불러왔습니다."),
    SUCCESS_DELETE("레시피를 성공적으로 삭제했습니다."),

    /**
     * ERROR
     */
    NOT_FOUND_MEMBER("사용자 정보를 찾을 수 없습니다."),
    NOT_FOUND_RECIPE("레시피를 찾을 수 없습니다."),
    NOT_FOUND_RECIPE_MANUAL("레시피 메뉴얼을 찾을 수 없습니다."),
    NO_ACCESS_TO_RECIPE("해당 레시피에 접근 권한이 없습니다."),
    EMPTY_RECIPE_LIST("레시피 목록이 비어있습니다."),
    ERROR_RECIPE_IMG_PROCESSING("이미지 파일 처리 중 오류가 발생했습니다.");

    private final String message;

    RecipeMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
