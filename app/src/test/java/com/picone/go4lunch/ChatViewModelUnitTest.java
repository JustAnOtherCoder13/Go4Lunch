package com.picone.go4lunch;

import com.picone.core.data.Generator;

import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ChatViewModelUnitTest extends BaseTest {

    @Test
    public void testNotNull(){
        assertNotNull(chatViewModel.getAllMessages);
        assertTrue(chatViewModel.getAllMessages.hasObservers());
    }

    @Test
    public void getAllMessagesShouldReturnListOfExistingMessages(){
        assertEquals(3, Objects.requireNonNull(chatViewModel.getAllMessages.getValue()).size());
        assertEquals(Generator.generateChatMessages().get(0).getUserText(),chatViewModel.getAllMessages.getValue().get(0).getUserText());
    }

    @Test
    public void postMessageShouldUpdateAllMessages(){
        chatViewModel.postMessage(messageToAdd);
        assertEquals(4, Objects.requireNonNull(chatViewModel.getAllMessages.getValue()).size());
        assertEquals(messageToAdd.getUserText(),chatViewModel.getAllMessages.getValue().get(3).getUserText());
    }
}
