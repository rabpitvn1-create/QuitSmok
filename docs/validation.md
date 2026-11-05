# Kiểm tra bản Java 0.1

Ngày 2026-11-05.

* `testDebugUnitTest`: 24 kiểm thử, không lỗi, không bỏ qua.
* `lintDebug`: kiểm tra tương thích API và cấu hình Android.
* `assembleDebug`: tạo APK debug, minSdk 26, targetSdk 35.
* Đã kiểm tra riêng mã Java với Android SDK 35.

Chưa chạy trên emulator hoặc thiết bị thật trong phiên này. Kiểm thử Java không xác nhận chất lượng UX, hành vi SQLite trên thiết bị hoặc hiệu quả cai thuốc.

## Kiểm tra thiết bị trước phân phối thử

1. Boot đúng 5 màn; quay lại và xoay màn hình.
2. Ghi một điếu, chống chạm lặp, hoàn tác; đóng/mở app kiểm tra còn dữ liệu.
3. Đối chiếu tổng cao hơn log không tạo thời gian giả; log mới hạ trạng thái xác nhận.
4. Ngày chưa rõ không hiển thị như đã không hút.
5. Cơn thèm và Recovery không có countdown, cấp phép hút hay phán xét.
6. Xuất JSON, hủy xuất, thử nơi lưu không ghi được.
7. Xóa dữ liệu rồi khởi động lại, xác nhận trở về onboarding.
8. Thử Android 8.0 và Android hiện hành, font lớn, TalkBack, điều hướng cử chỉ.
9. Qua nửa đêm không tạo ngày sinh hoạt mới; đổi múi giờ không phân bổ lại dữ liệu.
10. Không có quyền Internet hay thông báo trong APK.
