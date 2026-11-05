# MVP v1 và lát cắt Java 0.1

## Bất biến sản phẩm

Kế hoạch giảm thuốc thích nghi cho người từng bỏ đột ngột nhưng thất bại. Chỉ phản hồi khi người dùng mở app hoặc chủ động báo thèm. Không push đến cữ, đếm thời gian nhịn, cữ hiển thị hay thông điệp cấp phép hút. Cữ đã qua mất; không tích lũy, bù, hút dồn, thưởng tự khai hoặc phạt vượt kế hoạch.

Mốc ngân sách Bk = ceil(B0 × rk), rk = [1, .8, .6, .4, .25, .125]. Nội suy tỷ lệ theo ngày. Khoảng giãn I = W/B. Lambda điều chỉnh tốc độ tiến triển, trong [0,1], bằng 0 khi giữ mức. Không tăng tốc để kịp D. Công thức không tự chuyển ngân sách về 0; bước D là riêng biệt.

## Luồng 0.1 đã triển khai

1. Boot: giới thiệu, số điếu/ngày, khoảng tới điếu đầu, giờ thức, mục tiêu. Không hỏi D.
2. Trang chính: số ghi nhận, trạng thái dữ liệu, chế độ quan sát.
3. Ghi một điếu: lưu thời điểm và múi giờ, một thao tác, hoàn tác lần vừa ghi trong phiên.
4. Cơn thèm: “Mình thử đổi nhịp một chút nhé?”, “Gợi ý cho tôi”, “Để tôi tự xử lý”. Không timer.
5. Đối chiếu: tổng xác nhận, tổng nhớ lại, chưa rõ; chọn ngày quá khứ, không ngày tương lai.
6. Recovery: không xóa tiến bộ, ghi điếu nếu chưa ghi, sàng lọc hai câu; bản này luôn tiếp tục quan sát.
7. Riêng tư: xuất JSON, xóa toàn bộ. Không có analytics.

## Dữ liệu

OBSERVED là log trực tiếp, CONFIRMED là tổng được đối chiếu, RECALLED là tổng nhớ lại, UNKNOWN là chưa biết tổng. Không trạng thái nào là xác minh sinh học. Tổng ngày không cộng thêm lần nữa vào log. Tổng nhỏ hơn số log bị từ chối. Khi có log mới sau đối chiếu, tổng cũ cộng chênh lệch log và hạ về OBSERVED. UNKNOWN không thành 0.

Ngày sinh hoạt được tính theo giờ thức và múi giờ đã lưu lúc onboarding; qua nửa đêm không tạo ngày mới. Bản đầu cố định hai giá trị này để tránh phân bổ lại lịch sử khi đi du lịch. Chưa hỗ trợ ca đổi liên tục.

## Chưa triển khai trong 0.1

Lịch cữ thực thi, xác nhận mức nền B0 sau quan sát, khoảng thức W, ngày D thích nghi, thực thể lần cai, phân loại LAPSE sau D, nội dung 4 tuần, chỉnh sửa từng log quá khứ, nhập khoảng ước lượng, tiền tiết kiệm và nhập lại bản xuất. Không trình bày các phần này như đã có.

Khi triển khai: D cụ thể nhưng chỉ thay sau xác nhận; không tăng tốc; một điếu sau D là LAPSE. Lịch sử giữ nguyên, chỉ tạo lần cai mới khi người dùng chủ động. Tiền S = sum(max(0,B0-Cd) × P/Q) trên ngày đủ dữ liệu, ghi rõ phạm vi và ước tính.

## Tham số cần rà soát trước kích hoạt kế hoạch

Thời lượng mỗi chặng, điều kiện dữ liệu đầy đủ, lambda từng mức, tần suất đánh giá và điều kiện chuyển D. Không dùng mặc định y khoa tự đặt. Xin W sau tuần quan sát, không suy ra giờ ngủ từ điếu cuối.

## OUT khỏi MVP

AI coach/chatbot, thông báo đến cữ, huy hiệu/XP/streak, cảnh báo xoay vòng, cộng đồng, B2B, subscription, tự kê liều NRT, % hồi phục cá nhân và “sống thêm X ngày”.
