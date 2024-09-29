package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResCreateSkillDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/skills")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("")
    @ApiMessage("create a new skill")
    public ResponseEntity<ResCreateSkillDTO> handleCreateSkill(@Valid @RequestBody Skill request)
            throws IdInvalidException {
        if (skillService.existsByName(request.getName())) {
            throw new IdInvalidException("Skill name is existed!");
        }

        Skill skill = skillService.handleCreateSkill(request);
        return ResponseEntity.ok(skillService.convertToResCreateSkill(skill));
    }

    @PutMapping("")
    @ApiMessage("update a new skill")
    public ResponseEntity<ResCreateSkillDTO> handleUpdateSkill(@Valid @RequestBody Skill request)
            throws IdInvalidException {
        Skill skill = skillService.findById(request.getId());

        if (skill == null) {
            throw new IdInvalidException("Skill is not existed!");
        }

        if (skillService.existsByName(request.getName())) {
            throw new IdInvalidException("Skill name is existed!");
        }

        skill.setName(request.getName());
        skillService.handleUpdateSkill(skill);
        return ResponseEntity.ok(skillService.convertToResCreateSkill(skill));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("delete a skill")
    public ResponseEntity<Void> handleDeleteSkill(@PathVariable("id") long id) throws IdInvalidException {
        if (skillService.findById(id) == null) {
            throw new IdInvalidException("Skill is not existed!");
        }

        skillService.handleDeleteSkill(id);
        return ResponseEntity.ok().body(null);
    }

    // Specification: để filter
    @GetMapping("")
    @ApiMessage("fetch all skills")
    public ResponseEntity<ResultPaginationDTO> fetchAllSkills(
            @Filter Specification<Skill> spec,
            Pageable pageable) {

        return ResponseEntity.ok(skillService.fetchAllSkills(spec, pageable));
    }

}
