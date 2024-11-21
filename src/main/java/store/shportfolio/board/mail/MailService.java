package store.shportfolio.board.mail;

import jakarta.validation.Valid;
import store.shportfolio.board.vo.SendEmailVO;
import store.shportfolio.board.vo.VerifyCodeVO;

public interface MailService {

    void sendMail(SendEmailVO sendEmailVO);
    Boolean verifyMail(VerifyCodeVO verifyCodeVOv);
}
