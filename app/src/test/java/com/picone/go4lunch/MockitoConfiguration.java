package com.picone.go4lunch;

import androidx.annotation.NonNull;

import org.mockito.Mockito;
import org.mockito.configuration.DefaultMockitoConfiguration;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import io.reactivex.Observable;
import io.reactivex.Single;

class MockitoConfiguration extends DefaultMockitoConfiguration {
    public Answer<Object> getDefaultAnswer() {
        return new ReturnsEmptyValues() {
            @Override
            public Object answer(InvocationOnMock inv) {
                Class<?> type = inv.getMethod().getReturnType();
                if (type.isAssignableFrom(Observable.class)) {
                    return Observable.error(createException(inv));
                } else if (type.isAssignableFrom(Single.class)) {
                    return Single.error(createException(inv));
                } else {
                    return super.answer(inv);
                }
            }
        };
    }

    @NonNull
    private RuntimeException createException(
            InvocationOnMock invocation) {
        String s = invocation.toString();
        return new RuntimeException(
                "No mock defined for invocation " + s);
    }
}
