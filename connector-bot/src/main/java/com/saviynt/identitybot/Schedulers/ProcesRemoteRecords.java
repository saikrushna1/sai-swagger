package com.saviynt.identitybot.Schedulers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.saviynt.identitybot.Service.DispatcherService;
import com.saviynt.identitybot.Util.Constants;
import com.saviynt.identitybot.Util.JobUtil;
import com.saviynt.identitybot.constants.STATUS;
import com.saviynt.identitybot.model.BotAgent;
import com.saviynt.identitybot.model.Job;
import com.saviynt.identitybot.repository.BotAgentRepository;
import com.saviynt.identitybot.repository.JobRepository;

/**
 * 
 * @author Srinivasa Reddy Challa, Raj Kumar
 * 
 *         Scheduler to pick up new jobs for execution by remote agent at
 *         certain interval from Database and process them
 *
 */

@Component
@EnableAsync
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ProcesRemoteRecords {
	final static Logger logger = Logger.getLogger(ProcesRemoteRecords.class);
	@Autowired
	DispatcherService dispatcherService;

	@Autowired
	JobRepository jobRepository;

	@Value("${scripts.dir}")
	String scriptsRootDir;

	@Value("${agent.port}")
	String agentSystemPort;

	@Value("${agent.scriptUploadUrl}")
	String agentScriptUploadUrl;

	@Value("${cobot.status.callback.url}")
	String cobotStatusCallbackURL;

	@Autowired
	BotAgentRepository botAgentRepository;

	@Value("${bot.agent.status}")
	Integer status;

	@Value("${cobot.agent.loadbalancing.status}")
	String cobotAgentLoadbalancingStatus;

	@Async
	@Scheduled(fixedRateString = "${remoteJobInterval}", initialDelay = 1000) public void scheduleFixedRateTaskAsync()
			throws Exception {
		Job job = null;
		try {

			//job = jobRepository.findFirst1ByStatusAndRemoteAgentIPIsNotNull(STATUS.Submitted.toString());
			if (job != null) {
				if (cobotAgentLoadbalancingStatus.equals(Constants.ON)) {
					if (job.getParalleExcecutionStatus().equals(Constants.OFF)) {
						if (job.getRemoteAgentIP() != null && String.valueOf(job.getRemoteAgentIP()).length() > 0) {
							BotAgent botAgent = botAgentRepository.findByAgentIdOrIpaddressWithLoadBalancer(
									job.getRemoteAgentIP(), job.getRemoteAgentIP());
							if (botAgent != null) {
								int numberOfInstances = botAgent.getNumberOfInstances();
								job.setBotAgent(botAgent.getSno());
								if(numberOfInstances<5) {
									botAgentRepository.updateBotAgentLoadBalancerFlagBySno(1, ++numberOfInstances,
											botAgent.getSno());
									int retryCount = job.getRetryCount();
									logger.info("started job " + job.getId());
									jobRepository.updateStatus(++retryCount, STATUS.Inprocess.toString(), job.getId(),
											botAgent.getSno(), botAgent.getIpaddress());
									String appJobID = JobUtil.getAppJobIDFromDBJobID(job.getId());
									dispatcherService.processJob( appJobID, job, botAgent.getIpaddress());
									logger.info("job " + job.getId() + " is successfully completed");
								}
								
							}
						} else {
							List<BotAgent> botAgentList = botAgentRepository.findByLastUpdatedTimestamp(status);
							String remoteIp = null;
							Long agentId = 0l;
							BotAgent botAgent = null;

							if (botAgentList.size() > 0) {
								botAgent = botAgentList.get(0);
								int rows =  botAgentRepository.updateBotAgentLoadBalancerFlagBySnoAndLoadbalancerFlag(1, 1, botAgent.getSno());
								logger.info("Updated Remote Rows ===> "+rows);
								if(rows>0) {
								remoteIp = botAgent.getIpaddress();
								agentId = botAgent.getSno();

								int retryCount = job.getRetryCount();
								logger.info("started job " + job.getId());
								jobRepository.updateStatus(++retryCount, STATUS.Inprocess.toString(), job.getId(),
										agentId, remoteIp);
								String appJobID = JobUtil.getAppJobIDFromDBJobID(job.getId());
								dispatcherService.processJob(appJobID, job, remoteIp
										);
								logger.info("job " + job.getId() + " is successfully completed");
								}
							}
						}
					} else if (job.getParalleExcecutionStatus().equals(Constants.ON)) {
						if (job.getRemoteAgentIP() != null) {
							if (String.valueOf(job.getRemoteAgentIP()).length() > 0) {

								BotAgent botAgent = botAgentRepository.findByAgentIdOrIpaddress(job.getRemoteAgentIP(),
										job.getRemoteAgentIP());
								if (botAgent != null) {
									List<Job> runningJobList = jobRepository
											.findByAgentSnoWithApplicatoinName( botAgent.getSno());
									if (runningJobList.size() > 0) {
										for(Job runningJob : runningJobList) {
										if (!job.getAppName().trim().equals(runningJob.getAppName().trim())) {
											job.setBotAgent(botAgent.getSno());
											int retryCount = job.getRetryCount();
											int numberOfInstances = botAgent.getNumberOfInstances();
											if(numberOfInstances<5) {
												botAgentRepository.updateBotAgentLoadBalancerFlagBySno(
														botAgent.getLoadBalancerFlag(), ++numberOfInstances,
														botAgent.getSno());
												logger.info("started job " + job.getId());
												jobRepository.updateStatus(++retryCount, STATUS.Inprocess.toString(),
														job.getId(), botAgent.getSno(), botAgent.getIpaddress());
												String appJobID = JobUtil.getAppJobIDFromDBJobID(job.getId());
												dispatcherService.processJob( appJobID, job,
														botAgent.getIpaddress());
												logger.info("job " + job.getId() + " is successfully completed");
												break;
											}
											
										}
										}
									} else {
										job.setBotAgent(botAgent.getSno());
										int retryCount = job.getRetryCount();
										logger.info("started job " + job.getId());
										int numberOfInstances = botAgent.getNumberOfInstances();
										if(numberOfInstances<5) {
											botAgentRepository.updateBotAgentLoadBalancerFlagBySno(
													botAgent.getLoadBalancerFlag(), ++numberOfInstances, botAgent.getSno());
											jobRepository.updateStatus(++retryCount, STATUS.Inprocess.toString(),
													job.getId(), botAgent.getSno(), botAgent.getIpaddress());
											String appJobID = JobUtil.getAppJobIDFromDBJobID(job.getId());
											dispatcherService.processJob( appJobID, job,
													botAgent.getIpaddress());
											logger.info("job " + job.getId() + " is successfully completed");
										}
										
									}

								}
							} else {
								runJobsWithLoadbalaneAndParallelExecution(job);
							}
						} else {
							runJobsWithLoadbalaneAndParallelExecution(job);
						}

					}
				} else if (cobotAgentLoadbalancingStatus.equals(Constants.OFF)) {
					if (job.getParalleExcecutionStatus().equals(Constants.OFF)) {
						if (job.getRemoteAgentIP() != null) {
							if (String.valueOf(job.getRemoteAgentIP()).length() > 0) {
								BotAgent botAgent = botAgentRepository.findByAgentIdOrIpaddressWithLoadBalancer(
										job.getRemoteAgentIP(), job.getRemoteAgentIP());
								if (botAgent != null) {
									job.setBotAgent(botAgent.getSno());
									int numberOfInstances = botAgent.getNumberOfInstances();
									if(numberOfInstances<5) {
										botAgentRepository.updateBotAgentLoadBalancerFlagBySno(1, ++numberOfInstances,
												botAgent.getSno());
										int retryCount = job.getRetryCount();
										logger.info("started job " + job.getId());
										jobRepository.updateStatus(++retryCount, STATUS.Inprocess.toString(), job.getId(),
												botAgent.getSno(), botAgent.getIpaddress());
										String appJobID = JobUtil.getAppJobIDFromDBJobID(job.getId());
										dispatcherService.processJob(appJobID, job,
												botAgent.getIpaddress());
										logger.info("job " + job.getId() + " is successfully completed");
									}
									
								}

							} else {
								jobRepository.updateStatusAndReason(STATUS.Fail.toString(), Constants.ERROR_0,
										job.getId());
							}
						} else {
							jobRepository.updateStatusAndReason(STATUS.Fail.toString(), Constants.ERROR_0,
									job.getId());
						}
					} else if (job.getParalleExcecutionStatus().equals(Constants.ON)) {
						if (job.getRemoteAgentIP() != null) {
							if (String.valueOf(job.getRemoteAgentIP()).length() > 0) {

								BotAgent botAgent = botAgentRepository.findByAgentIdOrIpaddress(job.getRemoteAgentIP(),
										job.getRemoteAgentIP());
								if (botAgent != null) {
									List<Job> runningJobList = jobRepository
											.findByAgentSnoWithApplicatoinName(botAgent.getSno());
									if (runningJobList.size() > 0) {
										for(Job runningJob :runningJobList) {
										if (!job.getAppName().trim().equals(runningJob.getAppName().trim())) {
											job.setBotAgent(botAgent.getSno());
											int retryCount = job.getRetryCount();
											int numberOfInstances = botAgent.getNumberOfInstances();
											if(numberOfInstances<5) {
												botAgentRepository.updateBotAgentLoadBalancerFlagBySno(
														botAgent.getLoadBalancerFlag(), ++numberOfInstances,
														botAgent.getSno());
												logger.info("started job " + job.getId());
												jobRepository.updateStatus(++retryCount, STATUS.Inprocess.toString(),
														job.getId(), botAgent.getSno(), botAgent.getIpaddress());
												String appJobID = JobUtil.getAppJobIDFromDBJobID(job.getId());
												dispatcherService.processJob( appJobID, job,
														botAgent.getIpaddress());
												logger.info("job " + job.getId() + " is successfully completed");
												break;
											}
											
										}
									}
									} else {
										job.setBotAgent(botAgent.getSno());
										int retryCount = job.getRetryCount();
										logger.info("started job " + job.getId());
										int numberOfInstances = botAgent.getNumberOfInstances();
										if(numberOfInstances<5) {
											botAgentRepository.updateBotAgentLoadBalancerFlagBySno(
													botAgent.getLoadBalancerFlag(), ++numberOfInstances, botAgent.getSno());
											jobRepository.updateStatus(++retryCount, STATUS.Inprocess.toString(),
													job.getId(), botAgent.getSno(), botAgent.getIpaddress());
											String appJobID = JobUtil.getAppJobIDFromDBJobID(job.getId());
											dispatcherService.processJob(appJobID, job,
													botAgent.getIpaddress());
											logger.info("job " + job.getId() + " is successfully completed");
										}
										
									}

								}
							} else {
								jobRepository.updateStatusAndReason(STATUS.Fail.toString(), Constants.ERROR_0,
										job.getId());
							}
						} else {
							jobRepository.updateStatusAndReason(STATUS.Fail.toString(), Constants.ERROR_0,
									job.getId());
						}

					}

				}

			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			try {
				if (job != null) {
					jobRepository.updateStatusAndReason(STATUS.Failed.toString(), e.getMessage(), job.getId());
				}
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
		}
	}
	public void runJobsWithLoadbalaneAndParallelExecution(Job job) {
		try {

			List<BotAgent> agents = botAgentRepository.findAllByNumberOfInstancesByAsc();
			for (BotAgent botAgent : agents) {
				List<Job> runningJobList = jobRepository.findByAgentSnoWithApplicatoinName(botAgent.getSno());
				if (runningJobList.size() > 0) {
					for(Job runningJob : runningJobList) {
					if(!job.getAppName().trim().equals(runningJob.getAppName().trim())) {
						job.setBotAgent(botAgent.getSno());
						int retryCount = job.getRetryCount();
						int numberOfInstances = botAgent.getNumberOfInstances();
						
						if(numberOfInstances<5) {
							botAgentRepository.updateBotAgentLoadBalancerFlagBySno(botAgent.getLoadBalancerFlag(),
									++numberOfInstances, botAgent.getSno());
							logger.info("started job " + job.getId());
							jobRepository.updateStatus(++retryCount, STATUS.Inprocess.toString(), job.getId(),
									botAgent.getSno(), botAgent.getIpaddress());
							String appJobID = JobUtil.getAppJobIDFromDBJobID(job.getId());
							dispatcherService.processJob(appJobID, job, botAgent.getIpaddress());
							logger.info("job " + job.getId() + " is successfully completed");
							break;	
						}
						
					}
					}
				} else {
					job.setBotAgent(botAgent.getSno());
					int retryCount = job.getRetryCount();
					logger.info("started job " + job.getId());
					int numberOfInstances = botAgent.getNumberOfInstances();
					if(numberOfInstances<5) {
						botAgentRepository.updateBotAgentLoadBalancerFlagBySno(botAgent.getLoadBalancerFlag(),
								++numberOfInstances, botAgent.getSno());
						jobRepository.updateStatus(++retryCount, STATUS.Inprocess.toString(), job.getId(),
								botAgent.getSno(), botAgent.getIpaddress());
						String appJobID = JobUtil.getAppJobIDFromDBJobID(job.getId());
						dispatcherService.processJob(appJobID, job, botAgent.getIpaddress());
						logger.info("job " + job.getId() + " is successfully completed");
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
