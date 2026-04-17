const storageMock = () => {
  let storage: {[key: string]: string} = {};

  // noinspection JSUnusedGlobalSymbols
  return {
    getItem: (key: string) => (key in storage ? storage[key] : null),
    setItem: (key: string, value: string) => (storage[key] = value || ''),
    removeItem: (key: string) => delete storage[key],
    clear: () => (storage = {})
  };
};

Object.defineProperty(window, 'localStorage', {value: storageMock()});
Object.defineProperty(window, 'sessionStorage', {value: storageMock()});
Object.defineProperty(window, 'scrollIntoView', {value: storageMock()});
Object.defineProperty(window, 'getComputedStyle', {
  value: () => ['-webkit-appearance']
});

// fix for TypeError: this.window.matchMedia is not a function
if (!Object.hasOwn(window, 'matchMedia')) {
  Object.defineProperty(window, 'matchMedia', {
    writable: true,
    value: jest.fn().mockImplementation(query => ({
      matches: false,
      media: query,
      onchange: null,
      addListener: jest.fn(),
      removeListener: jest.fn(),
      addEventListener: jest.fn(),
      removeEventListener: jest.fn(),
      dispatchEvent: jest.fn()
    }))
  });
}
