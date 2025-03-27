package daoImp;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import daoInterface.RoomAllocationDAO;
import model.RoomAllocation;
import model.Rooms;
import utils.EntityManageUtils;

import java.sql.Timestamp;
import java.util.List;

public class RoomAllocationDAOImp extends BaseDAOImp<RoomAllocation> implements RoomAllocationDAO {

    private EntityManager entityManager = EntityManageUtils.getEntityManager();

    private EntityTransaction entityTransaction = entityManager.getTransaction();
    public RoomAllocationDAOImp(){
        super(RoomAllocation.class);
    }

    @Override
    public Long getRoomOccupancy(Rooms roomId){
        return entityManager.createQuery(
                        "SELECT COUNT(ra) FROM RoomAllocation ra WHERE ra.roomId = :roomId AND ra.unallocationDate IS NULL", Long.class)
                .setParameter("roomId", roomId)
                .getSingleResult();
    }


    @Override
    public List<RoomAllocation> getUserAllocated(Long userId){
        try{
            return entityManager.createQuery("SELECT ra FROM RoomAllocation ra WHERE ra.studentId.id = :studentId ORDER BY ra.allocationDate DESC",RoomAllocation.class)
                    .setParameter("studentId", userId)
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public boolean disableRoomUnallocatedStudent(Long roomId, Timestamp unallocationDate){
        try{
            entityTransaction.begin();
            int updateRow = entityManager.createQuery("UPDATE RoomAllocation ra SET ra.unallocationDate = :unallocationDate WHERE ra.roomId.id = :roomId AND ra.unallocationDate IS NULL")
                    .setParameter("unallocationDate", unallocationDate)
                    .setParameter("roomId",roomId)
                    .executeUpdate();
            entityManager.flush();
            entityManager.clear();
            entityTransaction.commit();
            return updateRow >0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Long getCountOnlyAllocated() {
        return entityManager.createQuery("SELECT COUNT(*) FROM RoomAllocation ra WHERE ra.unallocationDate IS NULL",Long.class)
                .getSingleResult();
    }

    @Override
    public RoomAllocation getRecentUserRoomAllocation(Long userId) {
        try{
            return entityManager.createQuery("SELECT r FROM RoomAllocation r WHERE r.studentId.id = :studentId AND r.unallocationDate IS NULL", RoomAllocation.class)
                    .setParameter("studentId",userId)
                    .getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
    }
}
