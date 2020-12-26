package it.niedermann.nextcloud.deck.persistence.sync.adapters.db;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import it.niedermann.nextcloud.deck.model.Account;
import it.niedermann.nextcloud.deck.model.Board;
import it.niedermann.nextcloud.deck.model.User;
import it.niedermann.nextcloud.deck.model.full.FullBoard;
import it.niedermann.nextcloud.deck.model.interfaces.AbstractRemoteEntity;

import static it.niedermann.nextcloud.deck.persistence.sync.adapters.db.DeckDatabaseTestUtil.createAccount;
import static it.niedermann.nextcloud.deck.persistence.sync.adapters.db.DeckDatabaseTestUtil.createBoard;
import static it.niedermann.nextcloud.deck.persistence.sync.adapters.db.DeckDatabaseTestUtil.createUser;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DataBaseAdapterTest {

    private DeckDatabase db;
    private DataBaseAdapter adapter;

    @Before
    public void createAdapter() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final Constructor<DataBaseAdapter> constructor = DataBaseAdapter.class.getDeclaredConstructor(Context.class, DeckDatabase.class);
        if (Modifier.isPrivate(constructor.getModifiers())) {
            constructor.setAccessible(true);
            db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), DeckDatabase.class).build();
            adapter = constructor.newInstance(ApplicationProvider.getApplicationContext(), db);
        }
    }

    @After
    public void closeDb() {
        if (db != null) {
            db.close();
        }
    }

    @Test
    public void testCreate() {
        final Account account = createAccount(db.getAccountDao());
        final User user = createUser(db.getUserDao(), account);
        final Board board = createBoard(db.getBoardDao(), account, user);
        final FullBoard fetchedBoard = adapter.getFullBoardByLocalIdDirectly(account.getId(), board.getLocalId());

        assertEquals(board.getTitle(), fetchedBoard.getBoard().getTitle());
    }

    @Test
    public void testFillSqlWithListValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final User user1 = createUser(db.getUserDao(), createAccount(db.getAccountDao()));
        final StringBuilder builder = new StringBuilder();
        final List<Object> args = new ArrayList<>(1);
        final List<? extends AbstractRemoteEntity> entities = new ArrayList<AbstractRemoteEntity>(1) {{
            add(user1);
        }};

        Method m = DataBaseAdapter.class.getDeclaredMethod("fillSqlWithListValues", StringBuilder.class, List.class, List.class);
        m.setAccessible(true);
        m.invoke(adapter, builder, args, entities);
        assertEquals("?", builder.toString());
        assertEquals(user1.getLocalId(), args.get(0));
    }

}
