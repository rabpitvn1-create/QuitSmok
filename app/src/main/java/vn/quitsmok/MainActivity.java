package vn.quitsmok;

import android.app.*;
import android.os.Bundle;
import android.content.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.*;
import android.widget.*;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.*;
import vn.quitsmok.data.JournalStore;
import vn.quitsmok.domain.*;

/** First offline vertical slice. No notifications, smoking permissions or countdowns. */
public final class MainActivity extends Activity {
    private final int ink=Color.rgb(30,63,53), muted=Color.rgb(87,104,96), cream=Color.rgb(246,244,238);
    private LinearLayout body;
    private JournalStore store;
    private android.content.SharedPreferences prefs;
    private int step=0, baseline=10, first=0, wakeHour=7, wakeMinute=0, goal=0, suggestion=0;
    private long lastTap=0;
    private String undoId;
    private final String[] suggestions={
        "Thử đổi chỗ ngồi hoặc rời khỏi nơi thường hút thuốc.",
        "Uống một ít nước, rồi chọn một việc nhỏ để làm bằng tay.",
        "Nếu phù hợp với sức khỏe, đứng dậy và đi lại nhẹ nhàng.",
        "Nhắn cho một người bạn tin cậy nếu bạn muốn có người đồng hành."
    };
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);store=new JournalStore(this);prefs=getSharedPreferences("quiet",MODE_PRIVATE);
        if(state!=null){step=state.getInt("step");baseline=state.getInt("baseline",10);first=state.getInt("first");
            wakeHour=state.getInt("wakeHour",7);wakeMinute=state.getInt("wakeMinute");goal=state.getInt("goal");}
        if(prefs.getBoolean("ready",false))home();else onboarding();
    }
    @Override protected void onSaveInstanceState(Bundle state){super.onSaveInstanceState(state);
        state.putInt("step",step);state.putInt("baseline",baseline);state.putInt("first",first);
        state.putInt("wakeHour",wakeHour);state.putInt("wakeMinute",wakeMinute);state.putInt("goal",goal);}
    private int dp(int n){return (int)(n*getResources().getDisplayMetrics().density+.5f);}
    private void page(String eyebrow,String title,String intro){
        ScrollView scroll=new ScrollView(this);scroll.setFillViewport(true);scroll.setBackgroundColor(cream);
        body=new LinearLayout(this);body.setOrientation(LinearLayout.VERTICAL);body.setPadding(dp(24),dp(24),dp(24),dp(32));
        scroll.addView(body);setContentView(scroll);
        scroll.setOnApplyWindowInsetsListener((v,insets)->{
            body.setPadding(dp(24),dp(24)+insets.getSystemWindowInsetTop(),dp(24),dp(32)+insets.getSystemWindowInsetBottom());return insets;});
        scroll.requestApplyInsets();text(eyebrow,12,muted);text(title,30,ink);text(intro,16,muted);
    }
    private TextView text(String value,int size,int color){TextView t=new TextView(this);t.setText(value);t.setTextSize(size);t.setTextColor(color);
        t.setLineSpacing(dp(4),1);if(size>=24)t.setTypeface(null,Typeface.BOLD);
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.bottomMargin=dp(16);body.addView(t,p);return t;}
    private void button(String label,boolean primary,Runnable run){Button b=new Button(this);b.setText(label);b.setAllCaps(false);b.setTextSize(16);b.setMinHeight(dp(52));
        b.setTextColor(primary?Color.WHITE:ink);GradientDrawable bg=new GradientDrawable();bg.setColor(primary?ink:Color.WHITE);bg.setCornerRadius(dp(16));b.setBackground(bg);
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.bottomMargin=dp(12);body.addView(b,p);b.setOnClickListener(v->run.run());}
    private EditText number(String hint,String value){EditText e=new EditText(this);e.setSingleLine(true);e.setText(value);e.setHint(hint);e.setContentDescription(hint);
        e.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);e.setTextSize(22);e.setTextColor(ink);body.addView(e,new LinearLayout.LayoutParams(-1,dp(64)));return e;}
    private void choices(String[] values,int selected,java.util.function.IntConsumer change){RadioGroup group=new RadioGroup(this);
        for(int i=0;i<values.length;i++){RadioButton b=new RadioButton(this);b.setId(View.generateViewId());b.setText(values[i]);b.setTextSize(16);b.setTextColor(ink);b.setMinHeight(dp(52));group.addView(b);if(i==selected)b.setChecked(true);}
        group.setOnCheckedChangeListener((g,id)->change.accept(g.indexOfChild(g.findViewById(id))));body.addView(group);}
    private void onboarding(){
        String[] titles={"Bỏ thuốc trong thầm lặng","Bắt đầu từ nhịp hiện tại","Điếu đầu trong ngày","Giờ thức thường ngày","Mục tiêu của bạn"};
        page("LÀM QUEN · "+(step+1)+"/5",titles[step],step==0?"Không nhắc đến cữ. Không bảng thành tích. Khi cần, bạn chủ động mở ứng dụng.":"Bạn có thể xem lại thông tin trong phần dữ liệu.");
        if(step==0){text("Ứng dụng hỗ trợ ghi nhận và thay đổi thói quen, không thay thế chẩn đoán hoặc điều trị. Bản thử nghiệm dành cho người trưởng thành.",16,muted);
            button("Thông tin an toàn",false,this::safety);button("Bắt đầu",true,()->{step++;onboarding();});}
        if(step==1){text("Thông thường mỗi ngày bạn hút bao nhiêu điếu?",18,ink);EditText n=number("Số điếu mỗi ngày",String.valueOf(baseline));
            button("Tiếp tục",true,()->{try{int v=Integer.parseInt(n.getText().toString());if(v<0||v>500)throw new NumberFormatException();baseline=v;step++;onboarding();}catch(NumberFormatException ex){n.setError("Nhập số từ 0 đến 500");}});}
        if(step==2){text("Sau khi thức dậy bao lâu bạn thường hút điếu đầu?",18,ink);
            choices(new String[]{"Trong 5 phút","6 đến 30 phút","31 đến 60 phút","Sau hơn 60 phút","Không hút hằng ngày"},first,v->first=v);
            button("Tiếp tục",true,()->{step++;onboarding();});}
        if(step==3){button(String.format(java.util.Locale.ROOT,"Giờ thức: %02d:%02d",wakeHour,wakeMinute),false,()->new TimePickerDialog(this,(v,h,m)->{wakeHour=h;wakeMinute=m;onboarding();},wakeHour,wakeMinute,true).show());
            text("Ca qua đêm được nhóm theo giờ thức, không tự tạo một ngày mới lúc nửa đêm.",16,muted);
            button("Tiếp tục",true,()->{step++;onboarding();});}
        if(step==4){choices(new String[]{"Giảm dần để tiến tới không hút","Quan sát thói quen trước","Duy trì không hút"},goal,v->goal=v);
            text("Bảy ngày đầu chỉ quan sát. Không yêu cầu chọn ngày bỏ thuốc. Bản 0.1 chưa tự kích hoạt lịch giảm sau tuần đầu.",16,muted);
            button("Bắt đầu ghi nhận",true,()->{prefs.edit().putInt("baselineEstimate",baseline).putInt("first",first).putInt("wakeHour",wakeHour).putInt("wakeMinute",wakeMinute).putInt("goal",goal)
                .putString("zone",ZoneId.systemDefault().getId()).putString("startedAt",Instant.now().toString()).putBoolean("ready",true).apply();home();});}
        if(step>0)button("Quay lại",false,()->{step--;onboarding();});
    }
    private String today(){return JournalDay.at(Instant.now(),ZoneId.of(prefs.getString("zone",ZoneId.systemDefault().getId())),
        LocalTime.of(prefs.getInt("wakeHour",7),prefs.getInt("wakeMinute",0))).toString();}
    private void home(){
        DaySummary s=store.summary(today());
        page("KHOẢNG RIÊNG CỦA BẠN","Hôm nay, nhẹ nhàng thôi.","Ghi nhận điều đã xảy ra. Cần hỗ trợ thì mở ứng dụng.");
        text("Ngày sinh hoạt "+today(),14,muted);
        text(s.total+" điếu đã ghi nhận",28,ink);
        text(stateLabel(s.state)+" · Không có nghĩa là tổng cả ngày nếu chưa đối chiếu.",14,muted);
        text("Đang quan sát",20,ink);text("Chưa áp lịch giảm. Dữ liệu còn thiếu sẽ được giữ là chưa rõ.",16,muted);
        button("Ghi một điếu",true,this::log);
        if(undoId!=null)button("Hoàn tác lần ghi vừa rồi",false,()->{store.undo(undoId);undoId=null;home();});
        button("Tôi đang thèm",false,this::craving);
        button("Đối chiếu ngày",false,()->review(today()));
        button("Tôi vừa hút lại",false,this::recovery);
        button("Dữ liệu và riêng tư",false,this::settings);
        button("Thông tin an toàn",false,this::safety);
    }
    private String stateLabel(DataState state){switch(state){case CONFIRMED:return "Đã xác nhận";case RECALLED:return "Nhớ lại";case UNKNOWN:return "Chưa rõ tổng ngày";default:return "Đã ghi tại thời điểm phát sinh";}}
    private void log(){long now=android.os.SystemClock.elapsedRealtime();if(now-lastTap<1000)return;lastTap=now;
        undoId=store.add(today(),ZoneId.systemDefault().getId());home();Toast.makeText(this,"Đã ghi nhận",Toast.LENGTH_SHORT).show();}
    private void craving(){page("KHI BẠN CẦN","Tôi đang thèm","Mình thử đổi nhịp một chút nhé?");
        button("Gợi ý cho tôi",true,this::suggestion);button("Để tôi tự xử lý",false,this::home);}
    private void suggestion(){page("MỘT GỢI Ý NHỎ","Đổi nhịp một chút",suggestions[suggestion%suggestions.length]);
        button("Gợi ý khác",false,()->{suggestion++;suggestion();});button("Đóng",true,this::home);}
    private void review(String day){page("ĐỐI CHIẾU","Ngày "+day,"Chỉ xác nhận điều bạn biết. Không cần điền cho đủ.");
        text("Đã ghi từng điếu: "+store.count(day),16,muted);
        DaySummary summary=store.summary(day);EditText n=number("Tổng số điếu cả ngày",String.valueOf(summary.total));
        button("Xác nhận tổng",true,()->saveReview(day,n,DataState.CONFIRMED));
        button("Đây là số tôi nhớ lại",false,()->saveReview(day,n,DataState.RECALLED));
        button("Chưa rõ tổng ngày",false,()->{store.review(day,null,DataState.UNKNOWN);home();});
        button("Chọn ngày khác",false,()->{LocalDate d=LocalDate.parse(day);new DatePickerDialog(this,(v,y,m,a)->{LocalDate selected=LocalDate.of(y,m+1,a);
            if(selected.isAfter(LocalDate.parse(today())))Toast.makeText(this,"Không đối chiếu ngày tương lai",Toast.LENGTH_SHORT).show();else review(selected.toString());},d.getYear(),d.getMonthValue()-1,d.getDayOfMonth()).show();});
        button("Trở về",false,this::home);
    }
    private void saveReview(String day,EditText n,DataState state){try{int total=Integer.parseInt(n.getText().toString());if(total<0||total>500)throw new IllegalArgumentException("Nhập số từ 0 đến 500");store.review(day,total,state);home();}
        catch(IllegalArgumentException e){n.setError(e instanceof NumberFormatException?"Hãy nhập một số nguyên":e.getMessage());}}
    private void recovery(){page("QUAY LẠI NHẸ NHÀNG","Vẫn có thể tiếp tục","Một lần hút lại không xóa những gì bạn đã làm.");
        text("Nếu điếu này chưa có trong nhật ký, bạn có thể ghi một lần bên dưới.",16,muted);
        button("Ghi điếu vừa hút",true,()->{log();recoveryHelp();});button("Đã ghi rồi",false,this::recoveryHelp);button("Để sau",false,this::home);}
    private void recoveryHelp(){page("XEM LẠI NHỊP HIỆN TẠI","Điều gì đang khó?","Bản thử nghiệm giữ chế độ quan sát, không tự giảm thêm.");
        final SafetyGate.Answer[] a={SafetyGate.Answer.UNKNOWN,SafetyGate.Answer.UNKNOWN};
        text("Bạn có hút sâu hơn, sát nhau hơn hoặc dùng thêm nicotine để bù không?",18,ink);
        choices(new String[]{"Không rõ / bỏ qua","Không","Có"},0,v->a[0]=answer(v));
        text("Khó chịu có ảnh hưởng đến ngủ, làm việc hoặc sinh hoạt không?",18,ink);
        choices(new String[]{"Không rõ / bỏ qua","Không","Có"},0,v->a[1]=answer(v));
        button("Xem hướng hỗ trợ",true,()->{SafetyGate.Decision d=SafetyGate.evaluate(false,false,store.summary(today()).state==DataState.CONFIRMED,a[0],a[1]);
            new AlertDialog.Builder(this).setTitle("Tiếp tục quan sát").setMessage(d==SafetyGate.Decision.CONTINUE?"Chưa có tín hiệu cần giữ mức từ hai câu này. Bản thử nghiệm chưa kích hoạt lịch giảm.":"Chưa giảm thêm. Nếu khó chịu kéo dài hoặc ảnh hưởng sinh hoạt, hãy trao đổi với nhân viên y tế.").setPositiveButton("Về trang chính",(x,y)->home()).show();});
        button("Thông tin an toàn",false,this::safety);button("Đóng",false,this::home);
    }
    private SafetyGate.Answer answer(int v){return v==1?SafetyGate.Answer.NO:v==2?SafetyGate.Answer.YES:SafetyGate.Answer.UNKNOWN;}
    private void safety(){page("HỖ TRỢ PHÙ HỢP","Thông tin an toàn","Ứng dụng không chẩn đoán, không kê thuốc và không hướng dẫn liều nicotine.");
        text("Nếu đang khó thở nặng, ngất, co giật hoặc nghi ngộ độc nicotine: tìm trợ giúp y tế khẩn cấp ngay. Không chờ ghi nhật ký hoặc dùng gợi ý cơn thèm.",18,ink);
        text("Người chưa thành niên, đang mang thai hoặc cho con bú nên tìm hỗ trợ chuyên môn phù hợp. Nếu đang dùng thuốc điều trị hoặc sản phẩm nicotine khác, hãy trao đổi với bác sĩ hoặc dược sĩ khi thay đổi việc hút thuốc.",16,muted);
        text("Không tự thay đổi thuốc hay liều nicotine theo ứng dụng. Nếu khó chịu ảnh hưởng sinh hoạt, hãy tìm hỗ trợ y tế.",16,muted);
        button("Trở về",true,()->{if(prefs.getBoolean("ready",false))home();else onboarding();});}
    private void settings(){page("DỮ LIỆU CỦA BẠN","Riêng tư từ đầu","Nhật ký lưu trên thiết bị. Không tài khoản, không quảng cáo, không gửi dữ liệu lên máy chủ.");
        text("Mức hút khai ban đầu: "+prefs.getInt("baselineEstimate",0)+" điếu/ngày. Đây chưa phải mức nền đã xác nhận.",16,muted);
        text("Bản thử nghiệm chưa có khôi phục từ bản xuất. Gỡ ứng dụng sẽ mất dữ liệu cục bộ. Bản xuất chứa thông tin riêng tư; hãy chọn nơi lưu phù hợp.",16,muted);
        button("Xuất dữ liệu JSON",true,()->{Intent i=new Intent(Intent.ACTION_CREATE_DOCUMENT);i.setType("application/json");i.addCategory(Intent.CATEGORY_OPENABLE);i.putExtra(Intent.EXTRA_TITLE,"quitsmok-export.json");startActivityForResult(i,41);});
        button("Xóa toàn bộ dữ liệu",false,()->new AlertDialog.Builder(this).setTitle("Xóa dữ liệu trên máy?").setMessage("Nhật ký và thông tin làm quen sẽ bị xóa. Các file đã xuất không bị xóa.")
            .setNegativeButton("Hủy",null).setPositiveButton("Xóa dữ liệu",(d,v)->{store.clear();prefs.edit().clear().apply();undoId=null;step=0;baseline=10;first=0;wakeHour=7;wakeMinute=0;goal=0;onboarding();}).show());
        button("Trở về",false,this::home);}
    @Override protected void onActivityResult(int request,int result,Intent data){super.onActivityResult(request,result,data);
        if(request!=41||result!=RESULT_OK||data==null)return;
        try {org.json.JSONObject out=new org.json.JSONObject(store.exportJson());org.json.JSONObject profile=new org.json.JSONObject();
            for(java.util.Map.Entry<String,?> e:prefs.getAll().entrySet())profile.put(e.getKey(),e.getValue());out.put("profile",profile);
            try(OutputStream stream=getContentResolver().openOutputStream(data.getData())){if(stream==null)throw new java.io.IOException();stream.write(out.toString(2).getBytes(StandardCharsets.UTF_8));}
            Toast.makeText(this,"Đã xuất dữ liệu",Toast.LENGTH_SHORT).show();
        }catch(Exception e){Toast.makeText(this,"Chưa xuất được dữ liệu. Vui lòng thử lại.",Toast.LENGTH_LONG).show();}}
    @Override protected void onDestroy(){store.close();super.onDestroy();}
}
