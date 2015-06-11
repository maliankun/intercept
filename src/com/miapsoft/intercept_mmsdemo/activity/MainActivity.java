package com.miapsoft.intercept_mmsdemo.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.intercept_mmsdemo.R;

import android.app.Activity;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;

public class MainActivity extends Activity{

	EditText ed_mmscode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ed_mmscode=(EditText) findViewById(R.id.ed_mms);
		SmsContent content = new SmsContent(new Handler());
        //ע����ű仯����
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, content);
	}
	
	
	/**
     * �����������ݿ�
     */
    class SmsContent extends ContentObserver {

        private Cursor cursor = null;

        public SmsContent(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {

            super.onChange(selfChange);
            //��ȡ�ռ�����ָ������Ķ���
            cursor = managedQuery(Uri.parse("content://sms/inbox"), new String[]{"_id", "address", "read", "body"},
                    " address=? and read=?", new String[]{"10658464", "0"}, "_id desc");//��id���������date����Ļ����޸��ֻ�ʱ��󣬶�ȡ�Ķ��žͲ�׼��
            if (cursor != null && cursor.getCount() > 0) {
                ContentValues values = new ContentValues();
                values.put("read", "1");        //�޸Ķ���Ϊ�Ѷ�ģʽ
                cursor.moveToNext();
                int smsbodyColumn = cursor.getColumnIndex("body");
                String smsBody = cursor.getString(smsbodyColumn);
                ed_mmscode.setText(getDynamicPassword(smsBody));

            }

            //����managedQuery��ʱ�򣬲�����������close()������ ������Android 4.0+��ϵͳ�ϣ� �ᷢ������
            if(Build.VERSION.SDK_INT < 14) {
                cursor.close();
            }
        }
    }
    
    
    
    /**
     * ���ַ����н�ȡ����6λ����
     * ���ڴӶ����л�ȡ��̬����
     * @param str ��������
     * @return ��ȡ�õ���6λ��̬����
     */
    public static String getDynamicPassword(String str) {
        Pattern  continuousNumberPattern = Pattern.compile("[0-9\\.]+");
        Matcher m = continuousNumberPattern.matcher(str);
        String dynamicPassword = "";
        while(m.find()){
            if(m.group().length() == 6) {
                System.out.print(m.group());
                dynamicPassword = m.group();
            }
        }

        return dynamicPassword;
    }

	
	
	
	
	
}
