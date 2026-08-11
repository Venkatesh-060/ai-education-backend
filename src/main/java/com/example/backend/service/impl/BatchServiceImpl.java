package com.example.backend.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.dto.BatchStatisticsDTO;
import com.example.backend.model.Batch;
import com.example.backend.model.User;
import com.example.backend.repository.BatchRepo;
import com.example.backend.repository.UserRepo;
import com.example.backend.service.BatchService;

@Service
public class BatchServiceImpl implements BatchService {

    private final BatchRepo batchRepo;
    private final UserRepo userRepo;

    public BatchServiceImpl(
            BatchRepo batchRepo,
            UserRepo userRepo) {

        this.batchRepo = batchRepo;
        this.userRepo = userRepo;
    }

    // CREATE
    @Override
    public Batch createBatch(Batch batch) {

        if (batchRepo.existsByBatchName(batch.getBatchName())) {
            throw new RuntimeException(
                    "Batch with this name already exists");
        }

        batch.setStatus("ACTIVE");

        if (batch.getStudentIds() == null) {
            batch.setStudentIds(new ArrayList<>());
        }

        if (batch.getStudentNames() == null) {
            batch.setStudentNames(new ArrayList<>());
        }

        return batchRepo.save(batch);
    }

    // GET ALL
    @Override
    public List<Batch> getAllBatches() {

        return batchRepo.findAll();
    }

    // GET ONE
    @Override
    public Batch getBatch(String id) {

        return batchRepo.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Batch not found with id: " + id));
    }

    // UPDATE
    @Override
    public Batch updateBatch(
            String id,
            Batch batch) {

        Batch existing = getBatch(id);

        existing.setBatchName(batch.getBatchName());
        existing.setCourseName(batch.getCourseName());
        existing.setStartDate(batch.getStartDate());
        existing.setEndDate(batch.getEndDate());
        existing.setDescription(batch.getDescription());
        existing.setStatus(batch.getStatus());

        return batchRepo.save(existing);
    }

    // DELETE
    @Override
    public void deleteBatch(String id) {

        if (!batchRepo.existsById(id)) {
            throw new RuntimeException(
                    "Batch not found with id: " + id);
        }

        batchRepo.deleteById(id);
    }

    // ASSIGN TRAINER
    @Override
    public Batch assignTrainer(
            String batchId,
            String trainerId) {

        Batch batch = getBatch(batchId);

        User trainer = userRepo.findById(trainerId)
                .orElseThrow(() -> new RuntimeException(
                        "Trainer not found"));

        batch.setTrainerId(trainer.getId());

        batch.setTrainerName(
                trainer.getFirstName()
                        + " "
                        + trainer.getLastName());

        return batchRepo.save(batch);
    }

    // ALLOCATE STUDENTS
    @Override
    public Batch allocateStudents(
            String batchId,
            List<String> studentIds) {

        Batch batch = getBatch(batchId);

        List<String> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();

        for (String studentId : studentIds) {

            User student = userRepo.findById(studentId)
                    .orElseThrow(() -> new RuntimeException(
                            "Student not found: "
                                    + studentId));

            ids.add(student.getId());

            names.add(
                    student.getFirstName()
                            + " "
                            + student.getLastName());
        }

        batch.setStudentIds(ids);
        batch.setStudentNames(names);

        return batchRepo.save(batch);
    }

    // REMOVE STUDENT
    @Override
    public Batch removeStudent(
            String batchId,
            String studentId) {

        Batch batch = getBatch(batchId);

        if (batch.getStudentIds() != null) {

            int index = batch.getStudentIds()
                    .indexOf(studentId);

            if (index >= 0) {

                batch.getStudentIds()
                        .remove(index);

                if (batch.getStudentNames() != null
                        && batch.getStudentNames().size() > index) {

                    batch.getStudentNames()
                            .remove(index);
                }
            }
        }

        return batchRepo.save(batch);
    }

    // GET STUDENTS
    @Override
    public List<User> getStudents() {

        return userRepo.findByRole("STUDENT");
    }

    // GET TRAINERS
    @Override
    public List<User> getTrainers() {

        return userRepo.findByRole("TRAINER");
    }

    // STATISTICS
    @Override
    public BatchStatisticsDTO getStatistics() {

        List<Batch> batches = batchRepo.findAll();

        BatchStatisticsDTO dto = new BatchStatisticsDTO();

        dto.setTotalBatches(batches.size());

        long active = 0;
        long completed = 0;
        long inactive = 0;
        long students = 0;
        long trainers = 0;

        for (Batch batch : batches) {

            if ("ACTIVE".equalsIgnoreCase(
                    batch.getStatus())) {

                active++;

            } else if ("COMPLETED".equalsIgnoreCase(
                    batch.getStatus())) {

                completed++;

            } else if ("INACTIVE".equalsIgnoreCase(
                    batch.getStatus())) {

                inactive++;
            }

            if (batch.getStudentIds() != null) {

                students += batch.getStudentIds().size();
            }

            if (batch.getTrainerId() != null
                    && !batch.getTrainerId().isEmpty()) {

                trainers++;
            }
        }

        dto.setActiveBatches(active);
        dto.setCompletedBatches(completed);
        dto.setInactiveBatches(inactive);
        dto.setTotalStudentsAllocated(students);
        dto.setBatchesWithTrainer(trainers);

        return dto;
    }

    @Override
    public List<Batch> getBatchesByTrainer(String trainerId) {

        return batchRepo.findByTrainerId(trainerId);
    }
}