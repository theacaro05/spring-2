package com.johnson.spring.service;

import com.johnson.spring.model.AppConstants;
import com.johnson.spring.repository.ContactRepository;
import com.johnson.spring.model.Contact;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScope
//@SessionScope
//@RequestScope
@Service
@Slf4j
@Getter
@Setter
public class ContactService {
    @Autowired
    ContactRepository contactRepository;

    public boolean saveMessage(Contact contact) {
        contact.setStatus(AppConstants.OPEN);
        contact.setCreatedBy(AppConstants.ANONYMOUS);
        contact.setCreatedAt(LocalDateTime.now());
        Contact savedContact = contactRepository.save(contact);
        return savedContact != null;
    }

    public List<Contact> findMessagesWithOpenStatus() {
        List<Contact> contactMessages = new ArrayList<>();
        Iterable<Contact> all = contactRepository.findAll();
        all.forEach(contactMessages::add);
        return contactMessages.stream()
                .filter(contact -> contact.getStatus().equals(AppConstants.OPEN))
                .collect(Collectors.toList());
    }

    public boolean updateMsgStatus(int contactId, String updatedBy) {
        Optional<Contact> tmpOpt = contactRepository.findById(contactId);
        if (tmpOpt.isEmpty()) {
            return false;
        }
        contactRepository.deleteById(contactId);
        Contact tmpContact = tmpOpt.get();
        tmpContact.setStatus(AppConstants.CLOSE);
        tmpContact.setUpdatedBy(updatedBy);
        tmpContact.setUpdatedAt(LocalDateTime.now());

        Contact updatedContact = contactRepository.save(tmpContact);
        return updatedContact != null;
    }
}
