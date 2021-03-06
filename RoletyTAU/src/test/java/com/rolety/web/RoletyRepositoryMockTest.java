package com.rolety.web;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import com.rolety.domain.Rolety;
import com.rolety.repository.IRoletyRepository;
import com.rolety.repository.RoletyRepositoryImpl;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoletyRepositoryMockTest {

    IRoletyRepository roletyRepository;

    @Mock
    Connection connectionMock;

    @Mock
    PreparedStatement addMock;

    @Mock
    PreparedStatement getAllMock;

    @Mock
    PreparedStatement deleteMock;

    @Mock
    PreparedStatement getByIdMock;

    @Mock
    PreparedStatement updateMock;

    @Before
    public void setupDatabase() throws SQLException {
        when(connectionMock.prepareStatement("INSERT INTO Rolety (Name, Price, Size) VALUES (?, ?, ?)")).thenReturn(addMock);
        when(connectionMock.prepareStatement("SELECT Id, Name, Price, Size FROM Rolety")).thenReturn(getAllMock);
        when(connectionMock.prepareStatement("DELETE FROM Rolety WHERE Id = ?")).thenReturn(deleteMock);
        when(connectionMock.prepareStatement("UPDATE Rolety SET Name = ?, Price = ?, Size = ? WHERE Id = ?")).thenReturn(updateMock);
        when(connectionMock.prepareStatement("SELECT * FROM Rolety WHERE Id = ?")).thenReturn(getByIdMock);
        roletyRepository = new RoletyRepositoryImpl();
        roletyRepository.setConnection(connectionMock);

        verify(connectionMock).prepareStatement("INSERT INTO Rolety (Name, Price, Size) VALUES (?, ?, ?)");
        verify(connectionMock).prepareStatement("SELECT Id, Name, Price, Size FROM Rolety");
        verify(connectionMock).prepareStatement("DELETE FROM Rolety WHERE Id = ?");
        verify(connectionMock).prepareStatement("UPDATE Rolety SET Name = ?, Price = ?, Size = ? WHERE Id = ?");
        verify(connectionMock).prepareStatement("SELECT * FROM Rolety WHERE Id = ?");

    }

    @Test
    public void checkAdding() throws SQLException {
        when(addMock.executeUpdate()).thenReturn(1);
        Rolety wyborne = new Rolety();

        wyborne.setName("Wyborne");
        wyborne.setPrice(20);
        wyborne.setSize(22);

        assertEquals(1, roletyRepository.add(wyborne));
        verify(addMock, times(1)).setString(1, "Wyborne");
        verify(addMock, times(1)).setInt(2, 20);
        verify(addMock, times(1)).setInt(3, 22);
        verify(addMock).executeUpdate();
    }

    @Test
    public void checkDeleting() throws SQLException {
        when(deleteMock.executeUpdate()).thenReturn(1);
        assertEquals(1, roletyRepository.delete(1));
        verify(deleteMock,times(1)).setInt(1, 1);
        verify(deleteMock).executeUpdate();
    }

    abstract class AbstractResultSet implements ResultSet {
        int i = 0;

        @Override
        public int getInt(String s) throws SQLException {
            return 1;
        }

        @Override
        public String getString(String columnLabel1) throws SQLException {
            if (columnLabel1 == "Name") {
                return "Wyborne";
            } else {
                return "Mango";
            }
        }


        @Override
        public boolean next() throws SQLException {
            if (i == 1)

                return false;
            i++;
            return true;
        }
    }

    @Test
    public void checkUpdating() throws SQLException {
        when(updateMock.executeUpdate()).thenReturn(1);
        Rolety mango = new Rolety();

        mango.setName("Mango");
        mango.setSize(10);
        mango.setPrice(100);

        assertEquals(1, roletyRepository.update(mango,1));
        verify(updateMock).executeUpdate();
    }

    @Test
    public void checkGetById() throws SQLException {
        AbstractResultSet mockedResutSet = mock(AbstractResultSet.class);
        when(mockedResutSet.next()).thenCallRealMethod();
        when(mockedResutSet.getInt("Id")).thenCallRealMethod();
        when(mockedResutSet.getString("Name")).thenCallRealMethod();
        when(mockedResutSet.getInt("Price")).thenCallRealMethod();
        when(mockedResutSet.getInt("Size")).thenCallRealMethod();
        when(getByIdMock.executeQuery()).thenReturn(mockedResutSet);

        assertEquals(1, roletyRepository.getById(1).getId());

        verify(getByIdMock, times(1)).executeQuery();
        verify(mockedResutSet, times(1)).getInt("Id");
        verify(mockedResutSet, times(1)).getString("Name");
        verify(mockedResutSet, times(1)).getInt("Price");
        verify(mockedResutSet, times(1)).getInt("Size");
        verify(mockedResutSet, times(2)).next();
    }

    @Test
    public void getAllTest() throws SQLException {
        AbstractResultSet mockedResultSet = mock(AbstractResultSet.class);
        when(mockedResultSet.next()).thenCallRealMethod();
        when(mockedResultSet.getInt("Id")).thenCallRealMethod();
        when(mockedResultSet.getString("Name")).thenCallRealMethod();
        when(mockedResultSet.getInt("Price")).thenCallRealMethod();
        when(mockedResultSet.getInt("Size")).thenCallRealMethod();
        when(getAllMock.executeQuery()).thenReturn(mockedResultSet);

        assertEquals(1, roletyRepository.getAll().size());

        verify(getAllMock, times(1)).executeQuery();
        verify(mockedResultSet, times(1)).getInt("Id");
        verify(mockedResultSet, times(1)).getString("Name");
        verify(mockedResultSet, times(1)).getInt("Price");
        verify(mockedResultSet, times(1)).getInt("Size");
        verify(mockedResultSet, times(2)).next();
    }
}