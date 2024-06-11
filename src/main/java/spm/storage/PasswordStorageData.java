package spm.storage;

public class PasswordStorageData {
    private String title;
    private String username;
    private String password;
    private String url;
    private String port;

    public PasswordStorageData(String title, String username, String password, String url, String port) {
        this.title = title;
        this.username = username;
        this.password = password;
        this.url = url;
        this.port = port;
    }

    public PasswordStorageData() {
        this(null, null, null, null, null);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
