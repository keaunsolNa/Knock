export const alarmCategoryList = ['MOVIE', 'PERFORMING_ARTS'];

export const categoryToText = {
  MOVIE: '영화',
  PERFORMING_ARTS: '공연 예술',
};

export const alarmSettingList = ['ONE_HOUR', 'THR_HOUR', 'SIX_HOUR', 'TWE_HOUR', 'ONE_DAY', 'THR_DAY', 'SEV_DAY'];

export const alarmToText = {
  ZERO_HOUR: '설정안함',
  ONE_HOUR: '1시간 전',
  THR_HOUR: '3시간 전',
  SIX_HOUR: '6시간 전',
  TWE_HOUR: '12시간 전',
  ONE_DAY: '1일 전',
  THR_DAY: '3일 전',
  SEV_DAY: '7일 전',
};

export const performingArtsGenreList = [
  'THEATER',
  'MUSICAL',
  'CLASSICAL',
  'KOREAN_TRADITIONAL',
  'POPULAR_MUSIC',
  'WESTERN_KOREAN_DANCE',
  'POPULAR_DANCE',
  'CIRCUS_MAGIC',
  'COMPLEX',
  'UNKNOWN',
];

export const performingArtsGenreToText = {
  THEATER: '연극',
  MUSICAL: '뮤지컬',
  CLASSICAL: '클래식',
  KOREAN_TRADITIONAL: '국악',
  POPULAR_MUSIC: '대중 음악',
  WESTERN_KOREAN_DANCE: '서양,한국 무용',
  POPULAR_DANCE: '대중 무용',
  CIRCUS_MAGIC: '서커스 마술',
  COMPLEX: '복합',
  UNKNOWN: '기타',
};

// prettier-ignore
export const genreToLink = {
  "연극": 'theater',
  "뮤지컬": 'musical',
  '서양음악(클래식)': 'classical',
  '한국음악(국악)': 'koreanTraditional',
  "대중음악": 'popularMusic',
  '무용(서양/한국무용)': 'westernKoreanDance',
  "대중무용": 'popularDance',
  '서커스/마술': 'circusMagic',
  "복합": 'complex',
  "기타": 'unknown',
};

export const performingArtsArea = ['서울', '경기/인천', '충청/대전/세종', '강원도', '경상도', '전라/광주', '제주도', '기타'];

export const areaToCode = {
  서울특별시: '0',
  경기도: '1',
  인천광역시: '1',
  대전광역시: '2',
  충청남도: '2',
  충청북도: '2',
  세종특별자치도: '2',
  강원도: '3',
  강원특별자치도: '3',
  부산광역시: '4',
  대구광역시: '4',
  경상남도: '4',
  경상북도: '4',
  울산광역시: '4',
  광주광역시: '5',
  전라북도: '5',
  전라남도: '5',
  전북특별자치도: '5',
  제주특별자치도: '6',
  해외: '7',
};

export const prfStateToString = {
  UPCOMING: '공연예정',
  ONGOING: '공연중',
  COMPLETED: '공연완료',
  OPEN_RUN: '오픈런',
  LIMITED_RUN: '리미티드런',
  CLOSING_SOON: '마감임박',
  UNKNOWN: '알 수 없음',
};
