package com.example.demo.src.payment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.payment.model.GetPaymentMembershipRes;
import com.example.demo.src.payment.model.GetPaymentReq;
import com.example.demo.src.payment.model.GetPaymentDtlRes;
import com.example.demo.src.payment.model.PatchCreditReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/payment")
public class PaymentController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PaymentProvider paymentProvider;
    @Autowired
    private final PaymentService paymentService;
    @Autowired
    private final JwtService jwtService;


    public PaymentController(PaymentProvider paymentProvider, PaymentService paymentService, JwtService jwtService){
        this.paymentProvider = paymentProvider;
        this.paymentService = paymentService;
        this.jwtService = jwtService;
    }
    /**
     * 결제 상세 조회 API
     * [GET] /payment
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<List<GetPaymentDtlRes> > getPaymentDtl(@RequestBody GetPaymentReq getPaymentReq) {
        // Get Users
        try{
            int userId = getPaymentReq.getUserId();
            List<GetPaymentDtlRes> getPaymentDtlRes = paymentProvider.getPaymentDtl(userId);
            return new BaseResponse<>(getPaymentDtlRes,SUCCESS_PAYMENT_DTL_SEARCH);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 결제 상세 조회 API
     * [GET] /payment/membership
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/membership") // (GET) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<GetPaymentMembershipRes> getPaymentMembership() {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserId();

            GetPaymentMembershipRes getPaymentMembershipRes = paymentProvider.getPaymentMembership(userIdxByJwt);
            return new BaseResponse<>(getPaymentMembershipRes,SUCCESS_PAYMENT_MEMBERSHIP_SEARCH);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 결제정보 변경 API
     * [PATCH] /payment/credit
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/credit")
    public BaseResponse<String> modifyCredit(@RequestBody PatchCreditReq patchCreditReq){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserId();
            int userId = patchCreditReq.getUserId();
//            userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if(patchCreditReq.getCreditNo() == null || patchCreditReq.getCreditNo() == ""){
                return new BaseResponse<>(EMPTY_CREDIT_NO);
            }
            if(patchCreditReq.getExpiryDt() == null || patchCreditReq.getExpiryDt() == ""){
                return new BaseResponse<>(EMPTY_EXPIRY_DT);
            }
            if(patchCreditReq.getHolderNm() == null || patchCreditReq.getHolderNm() == ""){
                return new BaseResponse<>(EMPTY_HOLDER_NM);
            }
            if(patchCreditReq.getHolderYear() == null || patchCreditReq.getHolderYear() == ""){
                return new BaseResponse<>(EMPTY_HOLDER_YEAR);
            }
            if(patchCreditReq.getHolderMonth() == null || patchCreditReq.getHolderMonth() == ""){
                return new BaseResponse<>(EMPTY_HOLDER_MONTH);
            }
            if(patchCreditReq.getHolderDay() == null || patchCreditReq.getHolderDay() == ""){
                return new BaseResponse<>(EMPTY_HOLDER_DAY);
            }
            paymentService.modifyCredit(patchCreditReq);

            return new BaseResponse<>(SUCCESS_MODIFY_CREDIT);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
