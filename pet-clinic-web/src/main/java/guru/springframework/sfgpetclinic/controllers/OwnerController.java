package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RequestMapping("/owners")
@Controller
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @RequestMapping("/find")
    public String findOwners(Model model) {
        log.debug(">>> FIND_OWNERS: entered");
        log.debug(">>> FIND_OWNERS: using owners/findOwners template, new owner inside");
        model.addAttribute("owner", new Owner());
        log.debug(">>> FIND_OWNERS: finished");
        return "owners/findOwners";
    }

    @GetMapping("/{ownerId}")
    public ModelAndView showOwner(@PathVariable String ownerId) {
        log.debug(">>> SHOW_OWNERS: entered");
        log.debug(">>> SHOW_OWNERS: using owners/ownerDetails template and path variable " + ownerId);
        ModelAndView modelAndView = new ModelAndView("owners/ownerDetails");
        modelAndView.addObject(ownerService.findById(Long.valueOf(ownerId)));
        log.debug(">>> SHOW_OWNERS: finished");
        return modelAndView;
    }

    @GetMapping({"", "/", "/index", "/index.html"})
    public String processFindOwners(Owner owner, BindingResult result, Model model) {
        log.debug(">>> PROCESS_FIND_OWNERS: entered");
        log.debug(">>> PROCESS_FIND_OWNERS: owner input last name: " + owner.getLastName());
        if (owner.getLastName() == null) {
            owner.setLastName("");
            log.debug(">>> PROCESS_FIND_OWNERS: set owner empty string for null field");
        }

        List<Owner> ownerList = ownerService.findAllByLastName(owner.getLastName());
        log.debug(">>> PROCESS_FIND_OWNERS: performed search by last name, found owners: " + ownerList.size());
        if (ownerList.isEmpty()) {
            result.rejectValue("lastName", "notFound", "not found");
            log.debug(">>> PROCESS_FIND_OWNERS: result field changed, using owners/findOwners template");
            log.debug(">>> PROCESS_FIND_OWNERS: finished");
            return "owners/findOwners";
        }
        if (ownerList.size() == 1) {
            log.debug(">>> PROCESS_FIND_OWNERS: redirecting to owners/" + ownerList.get(0).getId());
            log.debug(">>> PROCESS_FIND_OWNERS: finished");
            return "redirect:owners/" + ownerList.get(0).getId();
        }
        log.debug(">>> PROCESS_FIND_OWNERS: adding model attribute selections");
        model.addAttribute("selections", ownerList);
        log.debug(">>> PROCESS_FIND_OWNERS: result field changed, using owners/ownersList template");
        log.debug(">>> PROCESS_FIND_OWNERS: finished");
        return "owners/ownersList";
    }
}
