package it.sal.disco.unimib.charityhub;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.sal.disco.unimib.charityhub.utils.SharedPreferencesUtil;

public class SharedPreferencesUtilTest {

    @Mock
    private SharedPreferences.Editor editor;

    @Mock
    private SharedPreferences sharedPreferences;

    @Mock
    private Context context;

    private SharedPreferencesUtil sharedPreferencesUtil;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences);
        when(sharedPreferences.edit()).thenReturn(editor);

        // Mock the behavior of getString to return "testValue" when anyString() and anyString() are passed as arguments
        when(sharedPreferences.getString(anyString(), anyString())).thenReturn("testValue");

        sharedPreferencesUtil = new SharedPreferencesUtil(context);
    }


    @Test
    public void testWriteStringData() {
        sharedPreferencesUtil.writeStringData("TEST_FILE_NAME", "testKey", "testValue");


        verify(editor).putString("testKey", "testValue");
        verify(editor).apply();
    }

    @Test
    public void testReadStringData() {

        String expectedValue = sharedPreferencesUtil.readStringData("TEST_FILE_NAME", "testKey");

        // Verify that getString is called with the correct arguments
        verify(sharedPreferences).getString("testKey", null);
        String actualValue = sharedPreferences.getString("testKey", null);

        assertEquals(actualValue, expectedValue);
    }


    @Test
    public void testDeleteAll() {
        sharedPreferencesUtil.deleteAll("TEST_FILE_NAME");

        verify(editor).clear();
        verify(editor).apply();
    }




}
