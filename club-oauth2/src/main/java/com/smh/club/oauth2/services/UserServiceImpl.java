package com.smh.club.oauth2.services;

import com.smh.club.oauth2.contracts.mappers.UserMapper;
import com.smh.club.oauth2.contracts.services.UserService;
import com.smh.club.oauth2.domain.entities.UserDetailsEntity;
import com.smh.club.oauth2.domain.repos.UserRepository;
import com.smh.club.oauth2.dto.CreateUserDto;
import com.smh.club.oauth2.dto.UserDetailsDto;
import com.smh.club.oauth2.dto.UserDto;
import com.smh.club.oauth2.responses.PagedDto;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl extends AbstractServiceBase implements UserService {
  private final UserRepository userRepo;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  // TODO: Come up with a better scheme
  private final String DEFAULT_PWD = "ChangeMeNow1234";

  @Override
  public PagedDto<UserDto> getUserPage(Pageable pageable) {

    var pageRequest = PageRequest.of(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        getSort(pageable.getSort()));

    var page = userMapper.toPage(userRepo.findAll(pageRequest));
    return PagedDto.of(page);
  }

  @Override
  public Optional<UserDto> getUser(long userId) {
    return userRepo.findById(userId).map(userMapper::toUserDto);
  }

  @Override
  public Optional<UserDetailsDto> getUserDetails(long userId) {
    return userRepo.findById(userId).map(userMapper::toUserDetailsDto);
  }

  @Override
  public UserDetailsDto createUser(CreateUserDto createUserDto) {
    var entity = userMapper.toUserDetailsEntity(createUserDto);
    var password = passwordEncoder.encode(DEFAULT_PWD);
    entity.setPassword(password);
    return userMapper.toUserDetailsDto(userRepo.save(entity));
  }

  @Override
  public void deleteUser(long userid) {
    userRepo.deleteById(userid);
  }

  @Override
  public void resetPassword(long userId) {
    if (!userRepo.existsById(userId)) {
      throw new UsernameNotFoundException("User with id: " + userId + " not found");
    }
    userRepo.updatePassword(userId, passwordEncoder.encode(DEFAULT_PWD));
  }

  @Override
  public Optional<UserDetailsDto> updateUserDetails(long id, UserDetailsDto userDetailsDto) {
    var user = userRepo.findById(id);
    return user.map(u -> userMapper.updateUserEntity(userDetailsDto, u))
        .map(userMapper::toUserDetailsDto);
  }

  @Override
  protected Sort getSort(Sort sort) {
    if (sort.isUnsorted()) {
      return sort;
    }

    var orders =
        sort.get()
            .map(o -> new Sort.Order(o.getDirection(),
                getSort(o.getProperty(), UserDto.class, UserDetailsEntity.class)
                    .orElseThrow(IllegalArgumentException::new))).toList();

    return Sort.by(orders);
  }

}
