package com.bluebox;

/**
 * Created by xuchen on 16/7/15.
 */
public class BoxResultHandler implements IBoxResultHandler<String> {

    private StringBuilder cacheResult = new StringBuilder();
    // 返回命令的前缀
    public static final String RESULT_PREV = "START";
    // 返回命令的结尾
    public static final String RESULT_END = "END";

    @Override
    public String getResult(byte[] data) {

//                    blueBoxStateListener.onReadBytes(data);
//                    byte[] readBuf = (byte[]) msg.obj;
        byte[] data2 = new byte[data.length];
        System.arraycopy(data, 0, data2, 0, data.length);

        String strRead = new String(data2);
        cacheResult.append(strRead);
       /* if (cacheResult.toString().indexOf(RESULT_PREV) >= 0
                && cacheResult.toString().indexOf(RESULT_END) >= 0) {*/

        String newResult = cacheResult.toString();/*.substring(cacheResult.toString().lastIndexOf(RESULT_PREV) + 5,
                    cacheResult.toString().lastIndexOf(RESULT_END));*/
//            Log.i("xuchen", "接收报文结束:截取的字符串- " + newResult);
//            Log.i("xuchen", "接收报文结束:未截取- " + cacheResult.toString());
        cacheResult = new StringBuilder();
        return newResult;
       /* }

        return "";*/
    }
}
