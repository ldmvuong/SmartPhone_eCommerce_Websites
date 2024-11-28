package vn.ute.smartphoneshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ute.smartphoneshop.entity.PaymentEntity;
import vn.ute.smartphoneshop.repository.PaymentRepository;
import vn.ute.smartphoneshop.service.IPaymentService;
@Service
public class PaymentServiceImpl implements IPaymentService {

    @Autowired
    PaymentRepository paymentRepository;
    @Override
    public PaymentEntity findPaymentMethod(String paymentName) {
        return paymentRepository.findByName(paymentName);
    }
}
