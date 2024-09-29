package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResCreateSkillDTO;
import vn.hoidanit.jobhunter.domain.response.ResSkillDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill handleCreateSkill(Skill request) {
        return skillRepository.save(request);
    }

    public Skill handleUpdateSkill(Skill request) {
        return skillRepository.save(request);
    }

    public boolean existsByName(String name) {
        return skillRepository.existsByName(name);
    }

    public ResCreateSkillDTO convertToResCreateSkill(Skill skill) {
        ResCreateSkillDTO res = new ResCreateSkillDTO();
        res.setId(skill.getId());
        res.setName(skill.getName());
        res.setCreatedAt(skill.getCreatedAt());
        res.setCreatedBy(skill.getCreatedBy());
        res.setUpdatedAt(skill.getUpdatedAt());
        res.setUpdatedBy(skill.getUpdatedBy());

        return res;
    }

    public Skill findById(long id) {
        Optional<Skill> skilOptional = skillRepository.findById(id);
        if (skilOptional.isPresent()) {
            return skilOptional.get();
        }
        return null;
    }

    public void handleDeleteSkill(long id) {
        // delete job (inside job_skill table)
        Optional<Skill> skillOptional = skillRepository.findById(id);
        Skill currentSkill = skillOptional.get();
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

        // delete skill
        skillRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAllSkills(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pageSkill = skillRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageSkill.getTotalPages());
        mt.setTotal(pageSkill.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResSkillDTO> listSkill = pageSkill.getContent()
                .stream().map(item -> new ResSkillDTO(
                        item.getId(),
                        item.getName(),
                        item.getCreatedAt(),
                        item.getUpdatedAt()))
                .collect(Collectors.toList());

        rs.setResult(listSkill);
        return rs;
    }

}
