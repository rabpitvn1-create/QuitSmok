# Safety Gate

Ứng dụng hỗ trợ hành vi, không chẩn đoán hoặc điều chỉnh liều. Hai câu: có hút sâu/sát nhau/dùng thêm nicotine để bù không; khó chịu có ảnh hưởng ngủ/làm việc/sinh hoạt không. Không rõ hoặc bỏ qua không được coi là không.

| Tình huống | Hành vi mục tiêu |
| --- | --- |
| Hút bù | HOLD, không giảm tiếp |
| Khó chịu ảnh hưởng sinh hoạt | HOLD, đề nghị hỗ trợ chuyên môn |
| Thiếu hoặc mâu thuẫn dữ liệu | HOLD, không tự điền |
| Vượt kế hoạch hoặc tái hút | Ghi trung tính, Recovery, không bù/phạt/reset D |
| Nhóm cần chuyên môn hoặc thuốc đang dùng | REVIEW, không kê NRT |
| Triệu chứng nguy hiểm | URGENT_SUPPORT, tìm trợ giúp y tế ngay |

Ưu tiên URGENT_SUPPORT > REVIEW > HOLD > CONTINUE. CONTINUE chỉ cho phép xem xét bước giảm, không đồng nghĩa được hút. Bản 0.1 không kích hoạt lịch dù Gate trả CONTINUE. Hai cờ khẩn cấp/chuyên môn có trong domain nhưng chưa có bảng sàng lọc tự động; thông tin hỗ trợ luôn truy cập được. App không tự phát hiện tình huống y tế.

Disclaimer bốn điểm của MVP: boot; trước kích hoạt lịch; khi mở hướng dẫn liên quan thuốc/NRT; khi cần review hoặc khẩn cấp. Bản 0.1 có boot, trang an toàn và thông điệp khi giữ mức; chưa có màn kích hoạt lịch hoặc hướng dẫn NRT.

## Nguồn tham chiếu cần tiếp tục rà soát

* https://www.who.int/publications/i/item/9789240096431
* https://www.cdc.gov/tobacco/campaign/tips/quit-smoking/7-common-withdrawal-symptoms/index.html
* https://www.cdc.gov/niosh/ershdb/emergencyresponsecard_29750028.html
* https://www.nhs.uk/pregnancy/keeping-well/stop-smoking/
* https://sps.nhs.uk/articles/considering-drug-interactions-with-smoking/

Các nguồn không xác nhận thuật toán riêng của QuitSmok. Rà soát chuyên môn nội dung và tham số trước thử nghiệm lịch giảm với người dùng thật.
