################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../AnimationFile.cpp \
../CharacterAnimatorSpark.cpp \
../PerlinNoise.cpp \
../simplexnoise.cpp 

OBJS += \
./AnimationFile.o \
./CharacterAnimatorSpark.o \
./PerlinNoise.o \
./simplexnoise.o 

CPP_DEPS += \
./AnimationFile.d \
./CharacterAnimatorSpark.d \
./PerlinNoise.d \
./simplexnoise.d 


# Each subdirectory must supply rules for building sources it contributes
%.o: ../%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -I../../Include -I../../include -I../../Dependencies/Horde3D/Horde3D/Source/Shared/ -O0 -g3 -Wall -c -fmessage-length=0 -std=c++0x -fPIC -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


