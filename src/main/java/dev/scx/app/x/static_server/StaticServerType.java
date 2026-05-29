package dev.scx.app.x.static_server;

enum StaticServerType {

    STATIC_FILES("STATIC-FILES"),
    SINGLE_FILE("SINGLE-FILE ");

    private final String display;

    StaticServerType(String display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return display;
    }

}
