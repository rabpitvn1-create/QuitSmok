package vn.quitsmok.data;
import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import org.json.*;
import java.time.*;
import java.util.UUID;
import vn.quitsmok.domain.DataState;
import vn.quitsmok.domain.DaySummary;

public final class JournalStore extends SQLiteOpenHelper {
    public JournalStore(Context context) { super(context, "quiet.db", null, 1); }
    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE cigarettes(id TEXT PRIMARY KEY, occurred_at TEXT NOT NULL, recorded_at TEXT NOT NULL, zone TEXT NOT NULL, day TEXT NOT NULL, state TEXT NOT NULL)");
        db.execSQL("CREATE TABLE reviews(day TEXT PRIMARY KEY, total INTEGER, count_at_review INTEGER NOT NULL, state TEXT NOT NULL)");
    }
    @Override public void onUpgrade(SQLiteDatabase db,int from,int to) {
        throw new IllegalStateException("Migration required; never discard journals");
    }
    public int count(String day) {
        try(Cursor c=getReadableDatabase().rawQuery("SELECT COUNT(*) FROM cigarettes WHERE day=?",new String[]{day})) { c.moveToFirst(); return c.getInt(0); }
    }
    public DaySummary summary(String day) {
        int observed=count(day);
        try(Cursor c=getReadableDatabase().rawQuery("SELECT total,count_at_review,state FROM reviews WHERE day=?",new String[]{day})) {
            if(!c.moveToFirst()) return new DaySummary(observed,null,0,null);
            return new DaySummary(observed,c.isNull(0)?null:c.getInt(0),c.getInt(1),DataState.valueOf(c.getString(2)));
        }
    }
    public String add(String day,String zone) {
        String id=UUID.randomUUID().toString(),now=Instant.now().toString();
        ContentValues v=new ContentValues();v.put("id",id);v.put("occurred_at",now);v.put("recorded_at",now);
        v.put("zone",zone);v.put("day",day);v.put("state","OBSERVED");
        getWritableDatabase().insertOrThrow("cigarettes",null,v);return id;
    }
    public void undo(String id) { getWritableDatabase().delete("cigarettes","id=?",new String[]{id}); }
    public void review(String day,Integer total,DataState state) {
        int observed=count(day);
        if(total!=null && total<observed) throw new IllegalArgumentException("Tổng không thể nhỏ hơn số điếu đã ghi. Hãy kiểm tra nhật ký.");
        ContentValues v=new ContentValues();v.put("day",day);
        if(total==null)v.putNull("total");else v.put("total",total);
        v.put("count_at_review",observed);v.put("state",state.name());
        getWritableDatabase().insertWithOnConflict("reviews",null,v,SQLiteDatabase.CONFLICT_REPLACE);
    }
    public String exportJson() throws JSONException {
        JSONObject root=new JSONObject();root.put("schemaVersion",1);
        for(String table:new String[]{"cigarettes","reviews"}) {
            JSONArray rows=new JSONArray();
            try(Cursor c=getReadableDatabase().rawQuery("SELECT * FROM "+table,null)) {
                while(c.moveToNext()) { JSONObject row=new JSONObject();
                    for(int i=0;i<c.getColumnCount();i++) row.put(c.getColumnName(i),c.isNull(i)?JSONObject.NULL:c.getString(i));
                    rows.put(row);
                }
            }root.put(table,rows);
        }return root.toString(2);
    }
    public void clear() {
        SQLiteDatabase db=getWritableDatabase();db.beginTransaction();
        try { db.delete("cigarettes",null,null);db.delete("reviews",null,null);db.setTransactionSuccessful(); }
        finally { db.endTransaction(); }
    }
}
