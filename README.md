# QuitSmok · Bỏ thuốc trong thầm lặng

Ứng dụng Android native viết bằng **Java 17**, giao diện Android Views và SQLite cục bộ.

## Bản 0.1: lát cắt đầu tiên

* Boot flow 5 màn hình, tối đa 4 câu bắt buộc, không hỏi ngày D.
* Ghi một điếu tại thời điểm hiện tại, chống chạm lặp và hoàn tác lần vừa ghi.
* Đối chiếu tổng theo ngày hoặc nhập tổng nhớ lại; giữ trạng thái chưa rõ.
* Hỗ trợ cơn thèm theo yêu cầu, Recovery nhẹ nhàng sau hút lại.
* Xuất JSON qua trình chọn file của Android, xóa dữ liệu cục bộ.
* Không tài khoản, không Internet permission, không push, countdown, streak, quảng cáo hay subscription.

**Đây chưa phải toàn bộ MVP 8 tuần.** Máy lịch giảm chưa được kích hoạt. Sau 7 ngày, bản này tiếp tục quan sát; không tự đặt D, không tự giảm ngân sách. Công thức Java thuần và Safety Gate chỉ được kiểm thử như nền tảng cho giai đoạn tiếp theo. Không tuyên bố hiệu quả điều trị.

## Mở và chạy

1. Mở thư mục repo bằng Android Studio, dùng JDK 17.
2. Cài Android SDK Platform 35 và Build Tools 35.0.0.
3. Đồng bộ Gradle rồi Run trên Android 8.0 (API 26) trở lên.

```bash
./gradlew testDebugUnitTest lintDebug assembleDebug
```

Windows dùng `gradlew.bat`. APK debug nằm tại `app/build/outputs/apk/debug/app-debug.apk`. CI cũng tạo APK debug làm artifact cho người kiểm thử. APK này không phải bản phát hành đã ký.

## Kiến trúc

* `app/src/main/java/vn/quitsmok/MainActivity.java`: luồng giao diện đầu tiên.
* `app/src/main/java/vn/quitsmok/domain/`: công thức thử nghiệm, Safety Gate, ranh giới ngày sinh hoạt.
* `app/src/main/java/vn/quitsmok/data/`: SQLite, nhật ký và đối chiếu.
* `app/src/test/`: kiểm thử quy tắc Java thuần.
* `docs/`: đặc tả, giới hạn và kế hoạch tiếp theo.

Không đưa nhật ký thật, bản xuất hay khóa ký vào repo public. Backup Android được tắt. Gỡ app mất dữ liệu cục bộ; bản xuất chưa có chức năng nhập lại.

Xem [đặc tả](docs/product/mvp-v1.md), [an toàn](docs/safety/safety-gates.md) và [kế hoạch](docs/plan/eight-weeks.md).
