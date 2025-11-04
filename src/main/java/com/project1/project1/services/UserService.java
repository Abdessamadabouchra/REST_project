// package com.project1.project1.services;
// import com.project1.project1.dto.UserFullDto;
// import com.project1.project1.dto.UserPreviewDto;    
// import com.project1.project1.dto.mappers.UserMapper;
// import com.project1.project1.model.User;
// import com.project1.project1.repository.UserDao;
// import com.project1.project1.pyload.ListResponse;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.stereotype.Service; 

// @Service
// public class UserService {

//     private final UserDao userdao;
//     private final UserMapper userMapper;

//     public ListResponse<UserPreviewDto> getUsers(int page, int limit) {
//         Page<User> users = userdao.findAll(PageRequest.of(page, limit, Sort.by("registerDate")));
//         ListResponse<UserPreviewDto> response = new ListResponse<>();
//         response.setData(users.stream().map(userMapper::toPreviewDto).collect(Collectors.toList()));
//         response.setTotal(users.getTotalElements());
//         response.setPage(page);
//         response.setLimit(limit);
//         return response;
//     }

//     public UserFullDto getUserById(Long id) {
//         User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
//         return userMapper.toFullDto(user);
//     }

//     public UserFullDto createUser(UserFullDto dto) {
//         User user = userMapper.toEntity(dto);
//         user.setRegisterDate(LocalDate.now());
//         user = userRepository.save(user);
//         return userMapper.toFullDto(user);
//     }

//     public UserFullDto updateUser(Long id, UserFullDto dto) {
//         User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
//         user.setFirstName(dto.getFirstName());
//         user.setLastName(dto.getLastName());
//         user.setTitle(dto.getTitle());
//         user.setPhone(dto.getPhone());
//         user.setPicture(dto.getPicture());
//         user.setLocation(dto.getLocation());
//         userRepository.save(user);
//         return userMapper.toFullDto(user);
//     }

//     public Long deleteUser(Long id) {
//         userRepository.deleteById(id);
//         return id;
//     }
// }
