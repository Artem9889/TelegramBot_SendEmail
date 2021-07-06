package mail;

public class ExceptionEmail {

    private String auditEmail;

    public String getAuditEmail() {
        return auditEmail;
    }

    public void setAuditEmail(String auditEmail) {
        this.auditEmail = auditEmail;
    }


    public boolean checkEmail(){
        if (!getAuditEmail().contains("@") || !getAuditEmail().contains(".")){
            return true;
        }
        return false;
    }
}
