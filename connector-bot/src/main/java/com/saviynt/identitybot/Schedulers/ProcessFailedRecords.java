package com.saviynt.identitybot.Schedulers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 *         Scheduler to pick up Failed jobs at certain interval from Database
 *         and process them
 *
 */

@Component
@EnableAsync
public class ProcessFailedRecords {
	final static Logger logger = Logger.getLogger(ProcessFailedRecords.class);
	@Autowired
	DispatcherService dispatcherService;

	@Autowired
	JobRepository jobRepository;

	@Value("${cobot.job.retrycount}")
	int cobotJobRetrycount;

	@Autowired
	BotAgentRepository botAgentRepository;

	@Value("${bot.agent.status}")
	Integer status;

	@Value("${cobot.agent.loadbalancing.status}")
	String cobotAgentLoadbalancingStatus;

	@Async
	@Scheduled(fixedRateString = "${reTryJobInterval}", initialDelay = 1000)
	public void scheduleFixedRateTaskAsync() throws Exception {
		try {
			List<Job> jobs = jobRepository.findFirst1ByStatusOrRemoteAgentIPIsNotNull();
			for (Job job : jobs) {
				if (job != null) {
					
					if(job.getParalleExcecutionStatus().equalsIgnoreCase(Constants.ON)) {
						job.setRecordVideo(false);
					}
					if(job.getScriptType().equalsIgnoreCase(Constants.CLI) || job.getScriptType().equalsIgnoreCase(Constants.DESKTOP)){
						job.setParalleExcecutionStatus(Constants.OFF);
					}
					
					if (cobotAgentLoadbalancingStatus.equalsIgnoreCase(Constants.ON)) {
						if (job.getParalleExcecutionStatus().equalsIgnoreCase(Constants.OFF)) {
							if (job.getRemoteAgentIP() != null && String.valueOf(job.getRemoteAgentIP()).length() > 0) {
								BotAgent botAgent = botAgentRepository.findByAgentIdOrIpaddressWithLoadBalancer(
										job.getRemoteAgentIP(), job.getRemoteAgentIP());
								if (botAgent != null) {
									int numberOfInstances = botAgent.getNumberOfInstances();
									job.setBotAgent(botAgent.getSno());
									if (numberOfInstances < 5) {
										int retryCount = job.getRetryCount();
										if (job.getRetryCount() <= cobotJobRetrycount) {
											botAgentRepository.updateBotAgentLoadBalancerFlagBySno(1,
													++numberOfInstances, botAgent.getSno());

											logger.info("started job " + job.getId());
											if (job.getStatus().equals(STATUS.Submitted.toString())) {
												jobRepository.updateStatus(++retryCount, STATUS.Inprocess.toString(),
														job.getId(), botAgent.getSno(), botAgent.getIpaddress());
											} else if (job.getStatus().equals(STATUS.Failed.toString())) {
												jobRepository.updateStatus(++retryCount,
														STATUS.RetryInprogress.toString(), job.getId(),
														botAgent.getSno(), botAgent.getIpaddress());
											}

											String appJobID = JobUtil.getAppJobIDFromDBJobID(job.getId());
											dispatcherService.processJob( appJobID, job, botAgent.getIpaddress());
											logger.info("job " + job.getId() + " is successfully completed");
										} else {
											jobRepository.updateStatus(retryCount, STATUS.Fail.toString(), job.getId(),
													job.getBotAgent(), job.getAgentIp());
										}
									}

								}
							} else {
								List<BotAgent> botAgentList = botAgentRepository.findByLastUpdatedTimestamp(status);
								String remoteIp = null;
								Long agentId = 0l;
								BotAgent botAgent = null;

								if (botAgentList.size() > 0) {
									int retryCount = job.getRetryCount();
									if (job.getRetryCount() <= cobotJobRetrycount) {
										botAgent = botAgentList.get(0);
										int rows = botAgentRepository
												.updateBotAgentLoadBalancerFlagBySnoAndLoadbalancerFlag(1, 1,
														botAgent.getSno());
										logger.info("Updated Fail Rows ===> " + rows);
										if (rows > 0) {
											remoteIp = botAgent.getIpaddress();
											agentId = botAgent.getSno();
											job.setBotAgent(agentId);
											logger.info("started job " + job.getId());
											if (job.getStatus().equals(STATUS.Submitted.toString())) {
												jobRepository.updateStatus(++retryCount, STATUS.Inprocess.toString(),
														job.getId(), botAgent.getSno(), botAgent.getIpaddress());
											} else if (job.getStatus().equals(STATUS.Failed.toString())) {
												jobRepository.updateStatus(++retryCount,
														STATUS.RetryInprogress.toString(), job.getId(),
														botAgent.getSno(), botAgent.getIpaddress());
											}
											String appJobID = JobUtil.getAppJobIDFromDBJobID(job.getId());
											dispatcherService.processJob( appJobID, job, botAgent.getIpaddress());
											logger.info("job " + job.getId() + " is successfully completed");
										}
									} else {
										jobRepository.updateStatus(retryCount, STATUS.Fail.toString(), job.getId(),
												job.getBotAgent(), job.getAgentIp());
									}
								}
							}
						} else if (job.getParalleExcecutionStatus().equalsIgnoreCase(Constants.ON)) {
							if (Constants.WEB.equalsIgnoreCase(job.getScriptType())) {
								if (job.getRemoteAgentIP() != null) {
									if (String.valueOf(job.getRemoteAgentIP()).length() > 0) {

										BotAgent botAgent = botAgentRepository.findByAgentIdOrIpaddress(
												job.getRemoteAgentIP(), job.getRemoteAgentIP());
										if (botAgent != null) {
											List<Job> runningJobList = jobRepository
													.findByAgentSnoWithApplicatoinName(botAgent.getSno());
											if (runningJobList.size() > 0) {
												for (Job runningJob : runningJobList) {
													if (!job.getAppName().trim()
															.equals(runningJob.getAppName().trim())) {
														job.setBotAgent(botAgent.getSno());
														int retryCount = job.getRetryCount();
														if (job.getRetryCount() <= cobotJobRetrycount) {
															int numberOfInstances = botAgent.getNumberOfInstances();
															if (numberOfInstances < 5) {
																botAgentRepository.updateBotAgentLoadBalancerFlagBySno(
																		botAgent.getLoadBalancerFlag(),
																		++numberOfInstances, botAgent.getSno());
																logger.info("started job " + job.getId());
																if (job.getStatus()
																		.equals(STATUS.Submitted.toString())) {
																	jobRepository.updateStatus(++retryCount,
																			STATUS.Inprocess.toString(), job.getId(),
																			botAgent.getSno(), botAgent.getIpaddress());
																} else if (job.getStatus()
																		.equals(STATUS.Failed.toString())) {
																	jobRepository.updateStatus(++retryCount,
																			STATUS.RetryInprogress.toString(),
																			job.getId(), botAgent.getSno(),
																			botAgent.getIpaddress());
																}
																String appJobID = JobUtil
																		.getAppJobIDFromDBJobID(job.getId());
																dispatcherService.processJob( appJobID, job, botAgent.getIpaddress());
																logger.info("job " + job.getId()
																		+ " is successfully completed");
																break;
															}
														} else {
															jobRepository.updateStatus(retryCount,
																	STATUS.Fail.toString(), job.getId(),
																	job.getBotAgent(), job.getAgentIp());
														}
													}
												}
											} else {
												job.setBotAgent(botAgent.getSno());
												int retryCount = job.getRetryCount();
												if (job.getRetryCount() <= cobotJobRetrycount) {
													logger.info("started job " + job.getId());
													int numberOfInstances = botAgent.getNumberOfInstances();
													if (numberOfInstances < 5) {
														botAgentRepository.updateBotAgentLoadBalancerFlagBySno(
																botAgent.getLoadBalancerFlag(), ++numberOfInstances,
																botAgent.getSno());
														if (job.getStatus().equals(STATUS.Submitted.toString())) {
															jobRepository.updateStatus(++retryCount,
																	STATUS.Inprocess.toString(), job.getId(),
																	botAgent.getSno(), botAgent.getIpaddress());
														} else if (job.getStatus().equals(STATUS.Failed.toString())) {
															jobRepository.updateStatus(++retryCount,
																	STATUS.RetryInprogress.toString(), job.getId(),
																	botAgent.getSno(), botAgent.getIpaddress());
														}
														String appJobID = JobUtil.getAppJobIDFromDBJobID(job.getId());
														dispatcherService.processJob( appJobID, job, botAgent.getIpaddress());
														logger.info(
																"job " + job.getId() + " is successfully completed");
													}
												} else {
													jobRepository.updateStatus(retryCount, STATUS.Fail.toString(),
															job.getId(), job.getBotAgent(), job.getAgentIp());
												}
											}

										}
									} else {
										runJobsWithLoadbalaneAndParallelExecution(job);
									}
								} else {
									runJobsWithLoadbalaneAndParallelExecution(job);
								}
							} else {
								jobRepository.updateStatusAndReason(STATUS.Fail.toString(), Constants.ERROR_2,
										job.getId());
							}

						}
					} else if (cobotAgentLoadbalancingStatus.equalsIgnoreCase(Constants.OFF)) {
						if (job.getParalleExcecutionStatus().equalsIgnoreCase(Constants.OFF)) {
							if (job.getRemoteAgentIP() != null) {
								if (String.valueOf(job.getRemoteAgentIP()).length() > 0) {
									BotAgent botAgent = botAgentRepository.findByAgentIdOrIpaddressWithLoadBalancer(
											job.getRemoteAgentIP(), job.getRemoteAgentIP());
									if (botAgent != null) {
										job.setBotAgent(botAgent.getSno());
										int numberOfInstances = botAgent.getNumberOfInstances();
										if (numberOfInstances < 5) {
											int retryCount = job.getRetryCount();
											if (job.getRetryCount() <= cobotJobRetrycount) {
												botAgentRepository.updateBotAgentLoadBalancerFlagBySno(1,
														++numberOfInstances, botAgent.getSno());

												logger.info("started job " + job.getId());
												if (job.getStatus().equals(STATUS.Submitted.toString())) {
													jobRepository.updateStatus(++retryCount,
															STATUS.Inprocess.toString(), job.getId(), botAgent.getSno(),
															botAgent.getIpaddress());
												} else if (job.getStatus().equals(STATUS.Failed.toString())) {
													jobRepository.updateStatus(++retryCount,
															STATUS.RetryInprogress.toString(), job.getId(),
															botAgent.getSno(), botAgent.getIpaddress());
												}
												String appJobID = JobUtil.getAppJobIDFromDBJobID(job.getId());
												dispatcherService.processJob( appJobID, job, botAgent.getIpaddress());
												logger.info("job " + job.getId() + " is successfully completed");
											} else {
												jobRepository.updateStatus(retryCount, STATUS.Fail.toString(),
														job.getId(), job.getBotAgent(), job.getAgentIp());
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
						} else if (job.getParalleExcecutionStatus().equalsIgnoreCase(Constants.ON)) {
							if (job.getScriptType().equalsIgnoreCase(Constants.WEB)) {
								if (job.getRemoteAgentIP() != null) {
									if (String.valueOf(job.getRemoteAgentIP()).length() > 0) {

										BotAgent botAgent = botAgentRepository.findByAgentIdOrIpaddress(
												job.getRemoteAgentIP(), job.getRemoteAgentIP());
										if (botAgent != null) {
											List<Job> runningJobList = jobRepository
													.findByAgentSnoWithApplicatoinName(botAgent.getSno());
											if (runningJobList.size() > 0) {
												for (Job runningJob : runningJobList) {
													if (!job.getAppName().trim()
															.equals(runningJob.getAppName().trim())) {
														job.setBotAgent(botAgent.getSno());
														int retryCount = job.getRetryCount();
														if (job.getRetryCount() <= cobotJobRetrycount) {
															int numberOfInstances = botAgent.getNumberOfInstances();
															if (numberOfInstances < 5) {
																botAgentRepository.updateBotAgentLoadBalancerFlagBySno(
																		botAgent.getLoadBalancerFlag(),
																		++numberOfInstances, botAgent.getSno());
																logger.info("started job " + job.getId());
																if (job.getStatus()
																		.equals(STATUS.Submitted.toString())) {
																	jobRepository.updateStatus(++retryCount,
																			STATUS.Inprocess.toString(), job.getId(),
																			botAgent.getSno(), botAgent.getIpaddress());
																} else if (job.getStatus()
																		.equals(STATUS.Failed.toString())) {
																	jobRepository.updateStatus(++retryCount,
																			STATUS.RetryInprogress.toString(),
																			job.getId(), botAgent.getSno(),
																			botAgent.getIpaddress());
																}
																String appJobID = JobUtil
																		.getAppJobIDFromDBJobID(job.getId());
																dispatcherService.processJob( appJobID, job, botAgent.getIpaddress());
																logger.info("job " + job.getId()
																		+ " is successfully completed");
																break;
															}
														} else {
															jobRepository.updateStatus(retryCount,
																	STATUS.Fail.toString(), job.getId(),
																	job.getBotAgent(), job.getAgentIp());
														}
													}
												}
											} else {
												job.setBotAgent(botAgent.getSno());
												int retryCount = job.getRetryCount();
												if (job.getRetryCount() <= cobotJobRetrycount) {
													logger.info("started job " + job.getId());
													int numberOfInstances = botAgent.getNumberOfInstances();
													if (numberOfInstances < 5) {
														botAgentRepository.updateBotAgentLoadBalancerFlagBySno(
																botAgent.getLoadBalancerFlag(), ++numberOfInstances,
																botAgent.getSno());
														if (job.getStatus().equals(STATUS.Submitted.toString())) {
															jobRepository.updateStatus(++retryCount,
																	STATUS.Inprocess.toString(), job.getId(),
																	botAgent.getSno(), botAgent.getIpaddress());
														} else if (job.getStatus().equals(STATUS.Failed.toString())) {
															jobRepository.updateStatus(++retryCount,
																	STATUS.RetryInprogress.toString(), job.getId(),
																	botAgent.getSno(), botAgent.getIpaddress());
														}
														String appJobID = JobUtil.getAppJobIDFromDBJobID(job.getId());
														dispatcherService.processJob( appJobID, job, botAgent.getIpaddress());
														logger.info(
																"job " + job.getId() + " is successfully completed");
													}
												} else {
													jobRepository.updateStatus(retryCount, STATUS.Fail.toString(),
															job.getId(), job.getBotAgent(), job.getAgentIp());
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
							} else {
								jobRepository.updateStatusAndReason(STATUS.Fail.toString(), Constants.ERROR_2,
										job.getId());
							}
						}

					}

				}
			}
		} catch (Exception e) {
			logger.error(e);
		}

	}

	public void runJobsWithLoadbalaneAndParallelExecution(Job job) {
		try {

			List<BotAgent> agents = botAgentRepository.findAllByNumberOfInstancesByAsc();
			for (BotAgent botAgent : agents) {
				List<Job> runningJobList = jobRepository.findByAgentSnoWithApplicatoinName(botAgent.getSno());
				if (runningJobList.size() > 0) {
					for (Job runningJob : runningJobList) {
						if (!job.getAppName().trim().equals(runningJob.getAppName().trim())) {
							job.setBotAgent(botAgent.getSno());
							int retryCount = job.getRetryCount();
							if (job.getRetryCount() <= cobotJobRetrycount) {
								int numberOfInstances = botAgent.getNumberOfInstances();

								if (numberOfInstances < 5) {
									botAgentRepository.updateBotAgentLoadBalancerFlagBySno(
											botAgent.getLoadBalancerFlag(), ++numberOfInstances, botAgent.getSno());
									logger.info("started job " + job.getId());
									if (job.getStatus().equals(STATUS.Submitted.toString())) {
										jobRepository.updateStatus(++retryCount, STATUS.Inprocess.toString(),
												job.getId(), botAgent.getSno(), botAgent.getIpaddress());
									} else if (job.getStatus().equals(STATUS.Failed.toString())) {
										jobRepository.updateStatus(++retryCount, STATUS.RetryInprogress.toString(),
												job.getId(), botAgent.getSno(), botAgent.getIpaddress());
									}
									String appJobID = JobUtil.getAppJobIDFromDBJobID(job.getId());
									dispatcherService.processJob( appJobID, job, botAgent.getIpaddress());
									logger.info("job " + job.getId() + " is successfully completed");
									break;
								}
							} else {
								jobRepository.updateStatus(retryCount, STATUS.Fail.toString(), job.getId(),
										job.getBotAgent(), job.getAgentIp());
							}
						}
					}
				} else {
					job.setBotAgent(botAgent.getSno());
					int retryCount = job.getRetryCount();
					if (job.getRetryCount() <= cobotJobRetrycount) {
						logger.info("started job " + job.getId());
						int numberOfInstances = botAgent.getNumberOfInstances();
						if (numberOfInstances < 5) {
							botAgentRepository.updateBotAgentLoadBalancerFlagBySno(botAgent.getLoadBalancerFlag(),
									++numberOfInstances, botAgent.getSno());
							if (job.getStatus().equals(STATUS.Submitted.toString())) {
								jobRepository.updateStatus(++retryCount, STATUS.Inprocess.toString(), job.getId(),
										botAgent.getSno(), botAgent.getIpaddress());
							} else if (job.getStatus().equals(STATUS.Failed.toString())) {
								jobRepository.updateStatus(++retryCount, STATUS.RetryInprogress.toString(), job.getId(),
										botAgent.getSno(), botAgent.getIpaddress());
							}
							String appJobID = JobUtil.getAppJobIDFromDBJobID(job.getId());
							dispatcherService.processJob( appJobID, job, botAgent.getIpaddress());
							logger.info("job " + job.getId() + " is successfully completed");
						}
					} else {
						jobRepository.updateStatus(retryCount, STATUS.Fail.toString(), job.getId(), job.getBotAgent(),
								job.getAgentIp());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
