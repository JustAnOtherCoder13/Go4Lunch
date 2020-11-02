package com.picone.go4lunch;

import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

class MockitoUtils<T> {

    public OngoingStubbing <T> whenever(T methodCall){
        return Mockito.when(methodCall);
    }
}
