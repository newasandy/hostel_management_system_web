package daoImp;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import daoInterface.RoomDAO;
import model.Rooms;
import utils.EntityManageUtils;

import java.util.Collections;
import java.util.List;


public class RoomDAOImp extends BaseDAOImp<Rooms> implements RoomDAO {


    private EntityManager entityManager = EntityManageUtils.getEntityManager();

    public RoomDAOImp (){
        super(Rooms.class);
    }

    @Override
    public Rooms findByRoomNumber(int roomNumber){
        if (entityManager == null) {
            return null;
        }

        try{
            return entityManager.createQuery("SELECT r FROM Rooms r WHERE r.roomNumber = :roomNumber",Rooms.class)
                    .setParameter("roomNumber", roomNumber)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<Rooms> getAvailableRoom(){
        if (entityManager == null) {
            return Collections.emptyList();
        }

        try{
            return entityManager.createQuery("SELECT r FROM Rooms r WHERE r.status = TRUE AND r.capacity > "+ "(SELECT COUNT(ra) FROM RoomAllocation ra WHERE ra.roomId.id = r.id AND ra.unallocationDate IS NULL)",Rooms.class)
                    .getResultList();
        }catch (NoResultException e){
            return Collections.emptyList();
        }
    }

    @Override
    public Long getTotalCapacity() {
        if (entityManager == null) {
            return 0L;
        }

        try{
            return entityManager.createQuery("SELECT SUM(r.capacity) FROM Rooms r WHERE r.status = true",Long.class)
                    .getSingleResult();
        } catch (Exception e) {
            return 0L;
        }
    }
}