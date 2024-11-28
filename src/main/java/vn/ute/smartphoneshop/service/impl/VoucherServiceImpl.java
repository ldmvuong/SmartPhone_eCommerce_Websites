package vn.ute.smartphoneshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ute.smartphoneshop.entity.VoucherEntity;
import vn.ute.smartphoneshop.repository.VoucherRepository;
import vn.ute.smartphoneshop.service.IVoucherService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoucherServiceImpl implements IVoucherService {

    @Autowired
    private VoucherRepository voucherRepository;
    @Override
    public VoucherEntity findVoucherByCode(String code) {
        return voucherRepository.findByCode(code);
    }

    @Override
    public boolean isVoucherValid(VoucherEntity voucher) {
        if (voucher != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate expiryDate = LocalDate.parse(voucher.getExpiryDate(), formatter);
            LocalDate currentDate = LocalDate.now();
            // Kiểm tra nếu ngày hết hạn lớn hơn hoặc bằng ngày hiện tại
            return !expiryDate.isBefore(currentDate);
        }
        return false;
    }

    @Override
    public List<String> getAllVoucherNames() {
        List<VoucherEntity> vouchers = voucherRepository.findAll();
        return vouchers.stream()
                .map(VoucherEntity::getCode)
                .collect(Collectors.toList());
    }

    @Override
    public List<Float> getAllVoucherValues() {
        List<VoucherEntity> vouchers = voucherRepository.findAll();
        return vouchers.stream()
                .map(VoucherEntity::getDiscountPercent)
                .collect(Collectors.toList());
    }
}
