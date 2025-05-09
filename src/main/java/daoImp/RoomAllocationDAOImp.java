package daoImp;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import daoInterface.RoomAllocationDAO;
import model.RoomAllocation;
import model.Rooms;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Stateless
public class RoomAllocationDAOImp extends BaseDAOImp<RoomAllocation> implements RoomAllocationDAO, Serializable {

    public RoomAllocationDAOImp(){
        super(RoomAllocation.class);
    }

    @Override
    public Long getRoomOccupancy(Rooms roomId){
        if (entityManager == null) {
            return 0L;
        }

        try{
            Long result = entityManager.createQuery(
                            "SELECT COUNT(ra) FROM RoomAllocation ra WHERE ra.roomId = :roomId AND ra.unallocationDate IS NULL", Long.class)
                    .setParameter("roomId", roomId)
                    .getSingleResult();
            return result != null ? result : 0L;
        } catch (Exception e) {
            return 0L;
        }
    }


    @Override
    public List<RoomAllocation> getUserAllocated(Long userId){
        if (entityManager == null) {
            return Collections.emptyList();
        }

        try{
            return entityManager.createQuery("SELECT ra FROM RoomAllocation ra WHERE ra.studentId.id = :studentId ORDER BY ra.allocationDate DESC",RoomAllocation.class)
                    .setParameter("studentId", userId)
                    .getResultList();
        }catch (NoResultException e){
            return Collections.emptyList();
        }
    }

    @Override
    public boolean disableRoomUnallocatedStudent(Long roomId, Timestamp unallocationDate){
        if (entityManager == null) {
            return false;
        }

        try{
            int updateRow = entityManager.createQuery("UPDATE RoomAllocation ra SET ra.unallocationDate = :unallocationDate WHERE ra.roomId.id = :roomId AND ra.unallocationDate IS NULL")
                    .setParameter("unallocationDate", unallocationDate)
                    .setParameter("roomId",roomId)
                    .executeUpdate();
            return updateRow >0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Long getCountOnlyAllocated() {
        if (entityManager == null) {
            return 0L;
        }

        try{
            Long result = entityManager.createQuery("SELECT COUNT(ra) FROM RoomAllocation ra WHERE ra.unallocationDate IS NULL",Long.class)
                    .getSingleResult();
            return result != null ? result : 0L;
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override
    public RoomAllocation getRecentUserRoomAllocation(Long userId) {
        if (entityManager == null) {
            return null;
        }

        try{
            return entityManager.createQuery("SELECT r FROM RoomAllocation r WHERE r.studentId.id = :studentId AND r.unallocationDate IS NULL", RoomAllocation.class)
                    .setParameter("studentId",userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean unallocatedInactiveStudent(Long userId, Timestamp unallocationDate) {
        if (entityManager == null) {
            return false;
        }

        try{
            int updateRow = entityManager.createQuery("UPDATE RoomAllocation ra SET ra.unallocationDate = :unallocationDate WHERE ra.studentId.id = :studentId AND ra.unallocationDate IS NULL")
                    .setParameter("unallocationDate", unallocationDate)
                    .setParameter("studentId", userId)
                    .executeUpdate();
            return updateRow >0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
