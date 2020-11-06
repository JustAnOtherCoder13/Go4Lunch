package com.picone.go4lunch;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.picone.core.data.Generator;
import com.picone.core.data.repository.chat.ChatMessageRepository;
import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.data.repository.user.UserRepository;
import com.picone.core.domain.entity.ChatMessage;
import com.picone.core.domain.entity.predictionPOJO.Prediction;
import com.picone.core.domain.entity.predictionPOJO.PredictionResponse;
import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.core.domain.entity.restaurant.RestaurantPosition;
import com.picone.core.domain.entity.user.SettingValues;
import com.picone.core.domain.entity.user.User;
import com.picone.core.domain.interactors.SendNotificationInteractor;
import com.picone.core.domain.interactors.chatInteractors.GetAllMessagesInteractor;
import com.picone.core.domain.interactors.chatInteractors.PostMessageInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.placeInteractors.FetchRestaurantDetailFromPlaceInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.placeInteractors.FetchRestaurantDistanceInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.placeInteractors.FetchRestaurantFromPlaceInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.placeInteractors.GetPredictionInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurantInteractors.AddRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurantInteractors.GetAllPersistedRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurantInteractors.UpdateUserChosenRestaurantInteractor;
import com.picone.core.domain.interactors.usersInteractors.AddUserInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetAllUsersInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetCurrentUserForEmailInteractor;
import com.picone.core.domain.interactors.usersInteractors.UpdateUserInteractor;
import com.picone.core.utils.SchedulerProvider;
import com.picone.go4lunch.presentation.viewModels.ChatViewModel;
import com.picone.go4lunch.presentation.viewModels.RestaurantViewModel;
import com.picone.go4lunch.presentation.viewModels.UserViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.picone.core.utils.ConstantParameter.SETTING_START_VALUE;
import static org.mockito.Mockito.when;

abstract class BaseTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    //common values
    List<Restaurant> allRestaurants = new ArrayList<>(Generator.generateRestaurants());
    List<User> currentUsers = new ArrayList<>();
    List<User> allUsers = new ArrayList<>(Generator.generateUsers());
    List<ChatMessage> allMessages = new ArrayList<>(Generator.generateChatMessages());
    Prediction prediction = new Prediction();
    PredictionResponse predictionResponse = new PredictionResponse();
    final String locationStr = "location";
    final String GOOGLE_KEY = "key";

    Restaurant restaurantToAdd = new Restaurant("Chez Marco", "50m", "", new RestaurantPosition(), "avenue Marco", "13250", "10", "", "", new ArrayList<>(), new ArrayList<>());
    User userToAdd = new User("13012", "Marc", "marc@gmail.com", "", new ArrayList<>(), new SettingValues());
    ChatMessage messageToAdd = new ChatMessage("now","",userToAdd.getName(),"is there anyone who would like to eat burger?",userToAdd.getUid());


    SchedulerProvider schedulerProvider = new SchedulerProvider(Schedulers.trampoline(), Schedulers.trampoline());

    //mock UserViewModel
    UserViewModel userViewModel;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    GetAllUsersInteractor getAllUsersInteractor;
    @InjectMocks
    AddUserInteractor addUserInteractor;
    @InjectMocks
    UpdateUserInteractor updateUserInteractor;


    //mock RestaurantViewModel
    RestaurantViewModel restaurantViewModel;
    @Mock
    RestaurantRepository restaurantRepository;
    @InjectMocks
    AddRestaurantInteractor addRestaurantInteractor;
    @InjectMocks
    UpdateUserChosenRestaurantInteractor updateUserChosenRestaurantInteractor;
    @InjectMocks
    GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor;
    @InjectMocks
    GetAllPersistedRestaurantsInteractor getAllPersistedRestaurantsInteractor;
    @InjectMocks
    FetchRestaurantFromPlaceInteractor fetchRestaurantFromPlaceInteractor;
    @InjectMocks
    FetchRestaurantDistanceInteractor fetchRestaurantDistanceInteractor;
    @InjectMocks
    FetchRestaurantDetailFromPlaceInteractor fetchRestaurantDetailFromPlaceInteractor;
    @InjectMocks
    GetPredictionInteractor getPredictionInteractor;
    @InjectMocks
    SendNotificationInteractor sendNotificationInteractor;

    //mock ChatViewModel
    ChatViewModel chatViewModel;
    @Mock
    ChatMessageRepository chatMessageRepository;
    @InjectMocks
    GetAllMessagesInteractor getAllMessagesInteractor;
    @InjectMocks
    PostMessageInteractor postMessageInteractor;

   //mock observers
    @Mock
    Observer<List<Restaurant>> allDbRestaurantsObserver;
    @Mock
    Observer<User> currentUserObserver;
    @Mock
    Observer<List<User>> userObserver;
    @Mock
    Observer<List<Restaurant>> filteredRestaurantsObserver;
    @Mock
    Observer<List<User>> filteredUsersObserver;
    @Mock
    Observer<List<ChatMessage>> chatMessageObserver;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        //set Prediction response placeId
        prediction.setPlaceId(Generator.generateRestaurants().get(0).getPlaceId());
        List<Prediction> predictionList = new ArrayList<>();
        predictionList.add(prediction);
        predictionResponse.setPredictions(predictionList);

        //initViewModels
        restaurantViewModel = new RestaurantViewModel(addRestaurantInteractor, updateUserChosenRestaurantInteractor, getCurrentUserForEmailInteractor, fetchRestaurantFromPlaceInteractor
                , getAllPersistedRestaurantsInteractor, fetchRestaurantDistanceInteractor, fetchRestaurantDetailFromPlaceInteractor, getPredictionInteractor, sendNotificationInteractor, schedulerProvider);

        userViewModel = new UserViewModel(getAllUsersInteractor, addUserInteractor, updateUserInteractor, schedulerProvider);

        chatViewModel = new ChatViewModel(getAllMessagesInteractor,postMessageInteractor,schedulerProvider);

        //initObserver
        userViewModel.getAllUsers().observeForever(userObserver);

        restaurantViewModel.getAllDbRestaurants.observeForever(allDbRestaurantsObserver);
        restaurantViewModel.getCurrentUser.observeForever(currentUserObserver);
        restaurantViewModel.getAllRestaurants.observeForever(filteredRestaurantsObserver);
        restaurantViewModel.getAllFilteredUsers.observeForever(filteredUsersObserver);
        currentUsers.add(userToAdd);

        chatViewModel.getAllMessages.observeForever(chatMessageObserver);

        //mocks Repository returns
        //-----------------------------------------USER REPOSITORY-----------------------------------------------------------

        when(userRepository.addUser(userToAdd))
                .thenReturn(Completable.create(emitter ->{
                    //Must condition return value cause addUser override existing user with firebaseDatabase
                    if (!Objects.requireNonNull(userViewModel.getAllUsers().getValue()).contains(userToAdd))
                        Objects.requireNonNull(userViewModel.getAllUsers().getValue()).add(userToAdd);}));
        when(userRepository.updateUser(userToAdd))
                .thenReturn(Completable.create(emitter -> userToAdd.setSettingValues(SETTING_START_VALUE)));

        when(userRepository.getCurrentUserForEmail(userToAdd.getEmail()))
                .thenReturn(Observable.create(emitter -> emitter.onNext(currentUsers)));
        when(userRepository.getAllUsers())
                .thenReturn(Observable.create(emitter -> emitter.onNext(allUsers)));

        //-----------------------------------------RESTAURANT REPOSITORY-----------------------------------------------------------

        when(restaurantRepository.getAllPersistedRestaurants())
                .thenReturn(Observable.create(emitter -> emitter.onNext(allRestaurants)));
        when(restaurantRepository.addRestaurant(restaurantToAdd))
                .thenReturn(Completable.create(emitter -> {
            //Must condition return value cause addRestaurant override existing restaurant with firebaseDatabase
            if (!Objects.requireNonNull(restaurantViewModel.getAllDbRestaurants.getValue()).contains(restaurantToAdd))
                restaurantViewModel.getAllDbRestaurants.getValue().add(restaurantToAdd);
        }));
        when(restaurantRepository.updateUserChosenRestaurant(userToAdd))
                .thenReturn(Completable.create(CompletableEmitter::onComplete));
        when(restaurantRepository.updateUserChosenRestaurant(Generator.generateUsers().get(0)))
                .thenReturn(Completable.create(CompletableEmitter::onComplete));
        when(restaurantRepository.getPredictions(Generator.generateRestaurants().get(0).getName(),GOOGLE_KEY,locationStr))
                .thenReturn(Observable.create(emitter -> emitter.onNext(predictionResponse)));

        //-----------------------------------------CHAT REPOSITORY-----------------------------------------------------------

        when(chatMessageRepository.getAllMessages())
                .thenReturn(Observable.create(emitter -> emitter.onNext(allMessages)));
        when(chatMessageRepository.postMessage(messageToAdd))
                .thenReturn(Completable.create(emitter ->
                        Objects.requireNonNull(chatViewModel.getAllMessages.getValue()).add(messageToAdd)));

        //set viewModels initial values
        userViewModel.setAllDbUsers();
        restaurantViewModel.setAllDbRestaurants();
        restaurantViewModel.setCurrentUser(userToAdd.getEmail());
        restaurantViewModel.updateAllRestaurantsWithPersistedValues(allRestaurants);
        chatViewModel.setAllMessages();
    }
}
