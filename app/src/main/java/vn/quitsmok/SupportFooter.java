package vn.quitsmok;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Small opt-in footer. The original supplied QR image is bundled without modification. */
final class SupportFooter {
    private SupportFooter() {}

    static void addTo(Activity activity, LinearLayout parent, int textColor) {
        float density = activity.getResources().getDisplayMetrics().density;
        int gap = Math.round(12 * density);
        LinearLayout footer = new LinearLayout(activity);
        footer.setOrientation(LinearLayout.HORIZONTAL);
        footer.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        footer.setPadding(0, gap, 0, 0);

        TextView message = new TextView(activity);
        message.setText(R.string.support_message);
        message.setTextSize(13);
        message.setTextColor(textColor);
        message.setLineSpacing(3 * density, 1);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, -2, 1);
        textParams.setMarginEnd(gap);
        footer.addView(message, textParams);

        View thumbnail = new QrThumbnail(activity);
        thumbnail.setContentDescription(activity.getString(R.string.support_qr_description));
        thumbnail.setFocusable(true);
        thumbnail.setOnClickListener(v -> showOriginal(activity));
        int size = Math.round(72 * density);
        footer.addView(thumbnail, new LinearLayout.LayoutParams(size, size));
        parent.addView(footer, new LinearLayout.LayoutParams(-1, -2));
    }

    private static void showOriginal(Activity activity) {
        ImageView image = new ImageView(activity);
        image.setImageResource(R.drawable.donation_qr);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        image.setContentDescription(activity.getString(R.string.support_title));
        image.setBackgroundColor(Color.WHITE);
        int padding = Math.round(12 * activity.getResources().getDisplayMetrics().density);
        image.setPadding(padding, padding, padding, padding);
        // Bound the image to leave room for the title, close button and system bars.
        int height = Math.round(activity.getResources().getDisplayMetrics().heightPixels * .62f);
        LinearLayout container = new LinearLayout(activity);
        container.addView(image, new LinearLayout.LayoutParams(-1, height));
        new AlertDialog.Builder(activity)
                .setTitle(R.string.support_title)
                .setView(container)
                .setPositiveButton(R.string.support_close, null)
                .show();
    }

    /** Display only the QR region with its white quiet zone; never resynthesize the code. */
    private static final class QrThumbnail extends View {
        private final Bitmap original;
        private final Rect source;
        private final Rect target = new Rect();
        private final Paint paint = new Paint();

        QrThumbnail(Activity activity) {
            super(activity);
            original = BitmapFactory.decodeResource(activity.getResources(), R.drawable.donation_qr);
            // Region coordinates refer to the user's original 864 x 1536 image.
            source = new Rect(Math.round(original.getWidth() * 220f / 864),
                    Math.round(original.getHeight() * 372f / 1536),
                    Math.round(original.getWidth() * 644f / 864),
                    Math.round(original.getHeight() * 796f / 1536));
            paint.setFilterBitmap(false);
        }

        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(Color.WHITE);
            int edge = Math.min(getWidth(), getHeight());
            int left = (getWidth() - edge) / 2, top = (getHeight() - edge) / 2;
            target.set(left, top, left + edge, top + edge);
            canvas.drawBitmap(original, source, target, paint);
        }
    }
}
